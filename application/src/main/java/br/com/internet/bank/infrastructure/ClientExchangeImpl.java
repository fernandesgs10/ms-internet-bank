package br.com.internet.bank.infrastructure;

import br.com.internet.bank.config.MessageResourceConfig;
import br.com.internet.bank.entity.BalanceEntity;
import br.com.internet.bank.entity.ClientEntity;
import br.com.internet.bank.exchange.ClientExchange;
import br.com.internet.bank.generate.PageSort;
import br.com.internet.bank.repository.BalanceRepository;
import br.com.internet.bank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.assertj.core.util.Preconditions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ClientExchangeImpl implements ClientExchange {

    private final ClientRepository clientRepository;
    private final BalanceRepository balanceRepository;
    private final MessageResourceConfig messageResourceConfig;

    @Override
    public void createClient(Exchange exchange) {
        Object[] object = (Object[]) exchange.getIn().getBody();
        ClientEntity clientEntity = (ClientEntity) object[0];
        BigDecimal balance = (BigDecimal) object[1];
        String user = (String) object[2];
        clientEntity.setNmCreated(user);

        Preconditions.checkArgument(clientRepository.findClientByNomeAndNumeroConta(
                clientEntity.getNome(), clientEntity.getNumeroConta()).isEmpty(),
                messageResourceConfig.getMessage("client.name.numberaccount.already",
            clientEntity.getNome(), clientEntity.getNumeroConta()));

        ClientEntity clientEntity1 = clientRepository.save(clientEntity);

        List<BalanceEntity> listBalance = new ArrayList<>();
        BalanceEntity balanceEntity = new BalanceEntity();
        balanceEntity.setNmCreated(user);
        balanceEntity.setClient(clientEntity1);
        balanceEntity.setNumValue(balance);
        balanceEntity.setNumValueAnterior(balance);
        listBalance.add(balanceEntity);
        balanceRepository.saveAll(listBalance);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<ClientEntity> listClient(Exchange exchange) {
        Object[] object = (Object[]) exchange.getIn().getBody();
        Integer pageNo = (Integer) object[0];
        Integer pageSize = (Integer) object[1];
        List<String> sortBy = (List<String>) object[2];

        List<Sort.Order> sort = PageSort.getOrders(sortBy);
        Map<String, String> orderMap = new HashMap<>();
        orderMap.put("nmName", "nmName");

        PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by(sort));
        Pageable pg = PageSort.ajustarSort(paging, orderMap);
        Pageable pg1 = PageRequest.of(0, 1);
        Page<ClientEntity> page3 = clientRepository.findClientAll(pg);

         page3.forEach(clientEntity -> {
             Page<BalanceEntity> balanceEntityLastValue = balanceRepository.findBalanceLastValue(clientEntity.getCoSeqCliente(), pg1);
             Page<BalanceEntity> balanceEntityFirstValue = balanceRepository.findBalanceFirstValue(clientEntity.getCoSeqCliente(), pg1);

             //noinspection OptionalGetWithoutIsPresent
             clientEntity.setSaldo(balanceEntityLastValue.stream().findFirst().get().getNumValue());
             //noinspection OptionalGetWithoutIsPresent
             clientEntity.setSaldoAnterior(balanceEntityFirstValue.stream().findFirst().get().getNumValue());

         });

         return page3;
    }

}