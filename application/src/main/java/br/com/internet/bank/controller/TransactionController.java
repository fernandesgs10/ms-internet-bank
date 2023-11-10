package br.com.internet.bank.controller;


import br.com.internet.bank.api.DepositarDinheiro;
import br.com.internet.bank.api.SaqueDinheiro;
import br.com.internet.bank.api.TransacaoApi;
import br.com.internet.bank.api.Transaction;
import br.com.internet.bank.entity.ClientEntity;
import br.com.internet.bank.entity.PullMoneyEntity;
import br.com.internet.bank.entity.PushMoneyEntity;
import br.com.internet.bank.entity.TransactionEntity;
import br.com.internet.bank.generate.Page;
import br.com.internet.bank.mapper.ClientMapper;
import br.com.internet.bank.mapper.TransactionMapper;
import br.com.internet.bank.service.TransactionService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("ALL")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("${openapi.ms-internet-bank.base-path:/v1/internet-bank}/transacao")
@SecurityRequirement(name = "in-memory")
public class TransactionController extends Page implements TransacaoApi {

    private final ClientMapper clientMapper;
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    @Override
    @GetMapping("/saque-dinheiro/numValor/{numValor}")
    public ResponseEntity<br.com.internet.bank.api.Page> listTransacaoSaqueDinheiroPorNumValor(
            @ApiParam(value = "numValor transaction",required=true) @PathVariable("numValor") BigDecimal numValor,
            @Valid @RequestParam(value = "pageNo", required = false, defaultValue="0") Integer pageNo,
            @Valid @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
            @Valid @RequestParam(value = "sortBy", required = false) List<String> sortBy) {

        log.info("class=TransactionController method=listTransacaoSaqueDinheiroPorNumValor step=pageNo={}, pageSize={}, sortBy={},numValor={}",
                pageNo, pageSize, sortBy, numValor);

        org.springframework.data.domain.Page<TransactionEntity> page = transactionService.listTransacaoSaqueDinheiroPorNumValor(pageNo, pageSize, sortBy, numValor);

        org.springframework.data.domain.Page<Transaction> pageClientDto = convertPage(page,
                transactionMapper::converterObjectToTransaction);

        br.com.internet.bank.api.Page pageConvert =
                transactionMapper.converterToPageTransaction(pageClientDto);

        pageConvert.content(Collections.singletonList(pageClientDto.getContent()));

        log.info("class=TransactionController method=listTransacaoSaqueDinheiroPorNumValor step=end response{}", pageConvert);
        return ResponseEntity.status(HttpStatus.OK).body(pageConvert);

    }

    @Override
    @GetMapping("/depositar-dinheiro/numValor/{numValor}")
    public ResponseEntity<br.com.internet.bank.api.Page> listTransacaoDepositarDinheiroPorNumValor(
            @ApiParam(value = "numValor transaction",required=true) @PathVariable("numValor") BigDecimal numValor,
            @Valid @RequestParam(value = "pageNo", required = false, defaultValue="0") Integer pageNo,
            @Valid @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
            @Valid @RequestParam(value = "sortBy", required = false) List<String> sortBy) {

        log.info("class=TransactionController method=listTransacaoDepositarDinheiroPorNumValor step=pageNo={}, pageSize={}, sortBy={},numValor={}",
                pageNo, pageSize, sortBy, numValor);

        org.springframework.data.domain.Page<TransactionEntity> page = transactionService.listTransacaoDepositarDinheiroPorNumValor(pageNo, pageSize, sortBy, numValor);

        org.springframework.data.domain.Page<Transaction> pageClientDto = convertPage(page,
                transactionMapper::converterObjectToTransaction);

        br.com.internet.bank.api.Page pageConvert =
                transactionMapper.converterToPageTransaction(pageClientDto);

        pageConvert.content(Collections.singletonList(pageClientDto.getContent()));

        log.info("class=TransactionController method=listTransacaoDepositarDinheiroPorNumValor step=end response{}", pageConvert);
        return ResponseEntity.status(HttpStatus.OK).body(pageConvert);

    }


    @Override
    @GetMapping("/dtCreated/{dtCreated}")
    public ResponseEntity<br.com.internet.bank.api.Page> listTransacaoPorDtCreated(
            @ApiParam(value = "dtCreated transaction",required=true) @PathVariable("dtCreated") LocalDate dtCreated,
            @Valid @RequestParam(value = "pageNo", required = false, defaultValue="0") Integer pageNo,
            @Valid @RequestParam(value = "pageSize", required = false, defaultValue="20") Integer pageSize,
            @Valid @RequestParam(value = "sortBy", required = false) List<String> sortBy) {

        log.info("class=TransactionController method=listTransacaoPorDtCreated step=pageNo={}, pageSize={}, sortBy={},dtCreated={}",
                pageNo, pageSize, sortBy, dtCreated);

        org.springframework.data.domain.Page<TransactionEntity> page = transactionService.listarTransacaoPorData(pageNo, pageSize, sortBy, dtCreated);

        org.springframework.data.domain.Page<Transaction> pageClientDto = convertPage(page,
                transactionMapper::converterObjectToTransaction);

        br.com.internet.bank.api.Page pageConvert =
                transactionMapper.converterToPageTransaction(pageClientDto);

        pageConvert.content(Collections.singletonList(pageClientDto.getContent()));

        log.info("class=TransactionController method=listTransacaoPorDtCreated step=end response{}", pageConvert);
        return ResponseEntity.status(HttpStatus.OK).body(pageConvert);
    }


    @Override
    @PostMapping(value = "/depositar-dinheiro")
    public ResponseEntity<Void> depositarDinheiro(
            @ApiParam(value = "Optional description in pull money", required = true)
            @Valid @RequestBody DepositarDinheiro depositarDinheiro) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        log.info("class=TransactionController method=depositarDinheiro step=depositarDinheiro={}", depositarDinheiro);

        ClientEntity clientEntity =
                clientMapper.converterObjectToClientEntity(depositarDinheiro.getCliente());

        PullMoneyEntity pullMoneyEntity =
                transactionMapper.converterObjectToPullMoneyEntity(depositarDinheiro);
        pullMoneyEntity.setClient(clientEntity);

        transactionService.depositarDinheiro(pullMoneyEntity, user.getUsername());

        log.info("class=TransactionController method=depositarDinheiro step=end response");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @PutMapping(value = "/sacar-dinheiro")
    public ResponseEntity<Void> sacarDinheiro(
            @ApiParam(value = "Optional description in push money" ,required=true )
            @Valid @RequestBody SaqueDinheiro sacarDinheiro) {

        log.info("class=TransactionController method=sacarDinheiro step=sacarDinheiro={}", sacarDinheiro);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        ClientEntity clientEntity =
                clientMapper.converterObjectToClientEntity(sacarDinheiro.getCliente());

        PushMoneyEntity pushMoneyEntity =
                transactionMapper.converterObjectToPushMoneyEntity(sacarDinheiro);
        pushMoneyEntity.setClient(clientEntity);

        transactionService.sacarDinheiro(pushMoneyEntity, user.getUsername());

        log.info("class=TransactionController method=sacarDinheiro step=end response");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
