package br.com.internet.bank.infrastructure;

import br.com.internet.bank.common.NotFoundException;
import br.com.internet.bank.config.MessageResourceConfig;
import br.com.internet.bank.dto.ExtractDTO;
import br.com.internet.bank.entity.BalanceEntity;
import br.com.internet.bank.entity.PullMoneyEntity;
import br.com.internet.bank.entity.PushMoneyEntity;
import br.com.internet.bank.entity.TransactionEntity;
import br.com.internet.bank.exchange.TransactionExchange;
import br.com.internet.bank.generate.PageSort;
import br.com.internet.bank.repository.BalanceRepository;
import br.com.internet.bank.repository.ClientRepository;
import br.com.internet.bank.repository.PullMoneyRepository;
import br.com.internet.bank.repository.PushMoneyRepository;
import br.com.internet.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.assertj.core.util.Preconditions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static br.com.internet.bank.Utils.CalculateUtils.calculateRate;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TransactionExchangeImpl implements TransactionExchange {

    private final ClientRepository clientRepository;
    private final BalanceRepository balanceRepository;
    private final PushMoneyRepository pushMoneyRepository;
    private final PullMoneyRepository pullMoneyRepository;
    private final TransactionRepository transactionRepository;
    private final MessageResourceConfig messageResourceConfig;

    @SuppressWarnings("unchecked")
    @Override
    public Page<ExtractDTO> listTransacaoSaqueDinheiroPorNumValor(Exchange exchange) {
        Object[] object = (Object[]) exchange.getIn().getBody();
        Integer pageNo = (Integer) object[0];
        Integer pageSize = (Integer) object[1];
        List<String> sortBy = (List<String>) object[2];
        BigDecimal numValue = (BigDecimal) object[3];

        List<Sort.Order> sort = PageSort.getSortFormat(PageSort.getOrders(sortBy));
        Map<String, String> orderMap = new HashMap<>();
        orderMap.put("nome", "c.nome");

        PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by(sort));
        Pageable pg = PageSort.ajustarSort(paging, orderMap);

        Page<TransactionEntity> pageTransaction = transactionRepository.findTransactionPushMoneyByNumValue(numValue, pg);

        Pageable pg1 = PageRequest.of(0, 1);
        List<ExtractDTO> list = new ArrayList<>();
        Set<Long> set = new HashSet<>();
        pageTransaction.forEach(transaction -> {
            BalanceEntity balanceEntityLastValue = getBalanceEntityLastValue(transaction.getClient().getCoSeqCliente());
            BalanceEntity balanceEntityFirstValue = getBalanceEntityFirstValue(transaction.getClient().getCoSeqCliente());
            ExtractDTO extractDTO = new ExtractDTO();

            Page<PullMoneyEntity> pagePullMoneyEntity = pullMoneyRepository.findPullMoeyLastValue(transaction.getClient().getCoSeqCliente(), pg1);

            if(set.add(transaction.getClient().getCoSeqCliente())) {
                transaction.getClient().setSaldo(balanceEntityLastValue.getNumValue());
                transaction.getClient().setSaldoAnterior(balanceEntityFirstValue.getNumValue());

                if(!pagePullMoneyEntity.isEmpty()) {
                    //noinspection OptionalGetWithoutIsPresent
                    transaction.getClient()
                            .setSaldoResidual(pagePullMoneyEntity
                                    .stream().findFirst().get().getNumValor());
                }

                extractDTO.setClient(transaction.getClient());
                extractDTO.setSaqueDinheiro(pushMoneyRepository
                        .findPushByCoSeqClient(transaction.getClient().getCoSeqCliente()));
                extractDTO.setDepositarDinheiro(pullMoneyRepository.findPullByCoSeqClient(transaction.getClient().getCoSeqCliente()));

                list.add(extractDTO);
            }
        });

        return new PageImpl<>(list, paging, list.size());
    }


    @SuppressWarnings("unchecked")
    @Override
    public Page<ExtractDTO> listTransacaoDepositarDinheiroPorNumValor(Exchange exchange) {
        Object[] object = (Object[]) exchange.getIn().getBody();
        Integer pageNo = (Integer) object[0];
        Integer pageSize = (Integer) object[1];
        List<String> sortBy = (List<String>) object[2];
        BigDecimal numValue = (BigDecimal) object[3];

        List<Sort.Order> sort = PageSort.getSortFormat(PageSort.getOrders(sortBy));
        Map<String, String> orderMap = new HashMap<>();
        orderMap.put("nome", "c.nome");

        PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by(sort));
        Pageable pg = PageSort.ajustarSort(paging, orderMap);

        Page<TransactionEntity> pageTransaction = transactionRepository.findTransactionPullMoneyByNumValue(numValue, pg);

        Pageable pg1 = PageRequest.of(0, 1);
        List<ExtractDTO> list = new ArrayList<>();
        Set<Long> set = new HashSet<>();
        pageTransaction.forEach(transaction -> {
            BalanceEntity balanceEntityLastValue = getBalanceEntityLastValue(transaction.getClient().getCoSeqCliente());
            BalanceEntity balanceEntityFirstValue = getBalanceEntityFirstValue(transaction.getClient().getCoSeqCliente());
            ExtractDTO extractDTO = new ExtractDTO();

            Page<PullMoneyEntity> pagePullMoneyEntity = pullMoneyRepository.findPullMoeyLastValue(transaction.getClient().getCoSeqCliente(), pg1);

            if(set.add(transaction.getClient().getCoSeqCliente())) {
                transaction.getClient().setSaldo(balanceEntityLastValue.getNumValue());
                transaction.getClient().setSaldoAnterior(balanceEntityFirstValue.getNumValue());

                if(!pagePullMoneyEntity.isEmpty()) {
                    //noinspection OptionalGetWithoutIsPresent
                    transaction.getClient()
                            .setSaldoResidual(pagePullMoneyEntity
                                    .stream().findFirst().get().getNumValor());
                }

                extractDTO.setClient(transaction.getClient());
                extractDTO.setSaqueDinheiro(pushMoneyRepository
                        .findPushByCoSeqClient(transaction.getClient().getCoSeqCliente()));
                extractDTO.setDepositarDinheiro(pullMoneyRepository.findPullByCoSeqClient(transaction.getClient().getCoSeqCliente()));

                list.add(extractDTO);
            }
        });

        return new PageImpl<>(list, paging, list.size());
    }




    @SuppressWarnings("unchecked")
    @Override
    public Page<ExtractDTO> listarTransacaoPorData(Exchange exchange) {
        Object[] object = (Object[]) exchange.getIn().getBody();
        Integer pageNo = (Integer) object[0];
        Integer pageSize = (Integer) object[1];
        List<String> sortBy = (List<String>) object[2];
        LocalDate dtCreated = (LocalDate) object[3];

        Date dt = Date.from(dtCreated.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        List<Sort.Order> sort = PageSort.getSortFormat(PageSort.getOrders(sortBy));
        Map<String, String> orderMap = new HashMap<>();
        orderMap.put("nome", "c.nome");

        PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by(sort));
        Pageable pg = PageSort.ajustarSort(paging, orderMap);

        Page<TransactionEntity> pageTransaction = transactionRepository.findTransactionByDtCreated(dt, pg);

        Pageable pg1 = PageRequest.of(0, 1);
        List<ExtractDTO> list = new ArrayList<>();
        Set<Long> set = new HashSet<>();
        pageTransaction.forEach(transaction -> {
                BalanceEntity balanceEntityLastValue = getBalanceEntityLastValue(transaction.getClient().getCoSeqCliente());
                BalanceEntity balanceEntityFirstValue = getBalanceEntityFirstValue(transaction.getClient().getCoSeqCliente());
                ExtractDTO extractDTO = new ExtractDTO();

                Page<PullMoneyEntity> pagePullMoneyEntity = pullMoneyRepository.findPullMoeyLastValue(transaction.getClient().getCoSeqCliente(), pg1);

                if(set.add(transaction.getClient().getCoSeqCliente())) {
                    transaction.getClient().setSaldo(balanceEntityLastValue.getNumValue());
                    transaction.getClient().setSaldoAnterior(balanceEntityFirstValue.getNumValue());

                    if(!pagePullMoneyEntity.isEmpty()) {
                        //noinspection OptionalGetWithoutIsPresent
                        transaction.getClient()
                                .setSaldoResidual(pagePullMoneyEntity
                                        .stream().findFirst().get().getNumValor());
                    }

                    extractDTO.setClient(transaction.getClient());
                    extractDTO.setSaqueDinheiro(pushMoneyRepository
                            .findPushByCoSeqClient(transaction.getClient().getCoSeqCliente()));
                    extractDTO.setDepositarDinheiro(pullMoneyRepository.findPullByCoSeqClient(transaction.getClient().getCoSeqCliente()));

                    list.add(extractDTO);
                }
        });

        return new PageImpl<>(list, paging, list.size());
    }

    @Override
    public void depositarDinheiro(Exchange exchange) {
        Object[] object = (Object[]) exchange.getIn().getBody();
        PullMoneyEntity pullMoneyEntity = (PullMoneyEntity) object[0];
        String user = (String) object[1];
        pullMoneyEntity.setNmCreated(user);

        BalanceEntity balanceEntityLast = getBalanceEntityLastValue(pullMoneyEntity.getClient().getCoSeqCliente());
        BalanceEntity balanceEntityFirstValue = getBalanceEntityFirstValue(pullMoneyEntity.getClient().getCoSeqCliente());
        pullMoneyEntity.setNumValorAnterior(balanceEntityLast.getNumValueAnterior());

        BigDecimal calculate = pullMoneyEntity.getNumValor().add(balanceEntityLast.getNumValue());

        BalanceEntity balanceEntity1 = new BalanceEntity();
        balanceEntity1.setClient(pullMoneyEntity.getClient());
        balanceEntity1.setNumValue(calculate);
        balanceEntity1.setNumValueAnterior(balanceEntityFirstValue.getNumValueAnterior().add(calculate));

        balanceEntity1.setNmCreated(user);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setClient(pullMoneyEntity.getClient());
        transactionEntity.setNmCreated(user);

        transactionEntity.setDepositarDinheiro(pullMoneyEntity);
        pullMoneyRepository.save(pullMoneyEntity);
        transactionRepository.save(transactionEntity);
        balanceRepository.save(balanceEntity1);
    }

    @Override
    public void sacarDinheiro(Exchange exchange) {
        Object[] object = (Object[]) exchange.getIn().getBody();
        PushMoneyEntity pushMoneyEntity = (PushMoneyEntity) object[0];
        String user = (String) object[1];
        pushMoneyEntity.setNmCreated(user);

        BalanceEntity balanceEntityLastValue = getBalanceEntityLastValue(pushMoneyEntity.getClient().getCoSeqCliente());
        BalanceEntity balanceEntityFirstValue = getBalanceEntityFirstValue(pushMoneyEntity.getClient().getCoSeqCliente());
        pushMoneyEntity.setNumValorAnterior(balanceEntityLastValue.getNumValueAnterior());

        BigDecimal resultRate = calculateRate(pushMoneyEntity.getNumValor(), balanceEntityLastValue.getClient().getPlanoExclusive());
        BigDecimal calculateValue = balanceEntityLastValue.getNumValue().subtract(resultRate);
        boolean isCalculateValue = calculateValue.doubleValue() < 0;
        Preconditions.checkArgument(!isCalculateValue,
                messageResourceConfig.getMessage("balance.insufficient",
                        balanceEntityLastValue.getNumValue()));

        BalanceEntity balanceEntity1 = new BalanceEntity();
        balanceEntity1.setClient(pushMoneyEntity.getClient());
        balanceEntity1.setNumValue(calculateValue);
        balanceEntity1.setNumValueAnterior(balanceEntityFirstValue.getNumValueAnterior());
        balanceEntity1.setNmCreated(user);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setClient(pushMoneyEntity.getClient());
        transactionEntity.setNmCreated(user);

        transactionEntity.setSaqueDinheiro(pushMoneyEntity);
        pushMoneyRepository.save(pushMoneyEntity);
        transactionRepository.save(transactionEntity);
        balanceRepository.save(balanceEntity1);
    }

    private BalanceEntity getBalanceEntityLastValue(Long id) {
        Preconditions.checkArgument(clientRepository.findById(id).isPresent(),
                messageResourceConfig.getMessage("client.coSeqCliente.notfound",
                        id));

        Pageable pg1 = PageRequest.of(0, 1);
        return balanceRepository.findBalanceLastValue(id, pg1).stream().findFirst()
                .orElseThrow(() -> new NotFoundException(messageResourceConfig.getMessage("balance.notfound",
                        id)));
    }

    private BalanceEntity getBalanceEntityFirstValue(Long id) {
        Preconditions.checkArgument(clientRepository.findById(id).isPresent(),
                messageResourceConfig.getMessage("client.coSeqCliente.notfound",
                        id));

        Pageable pg1 = PageRequest.of(0, 1);
        return balanceRepository.findBalanceFirstValue(id, pg1).stream().findFirst()
                .orElseThrow(() -> new NotFoundException(messageResourceConfig.getMessage("balance.notfound",
                        id)));
    }
}