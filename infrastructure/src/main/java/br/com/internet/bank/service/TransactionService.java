package br.com.internet.bank.service;

import br.com.internet.bank.common.InternetBankException;
import br.com.internet.bank.common.NotFoundException;
import br.com.internet.bank.entity.PullMoneyEntity;
import br.com.internet.bank.entity.PushMoneyEntity;
import br.com.internet.bank.enums.TransactionRouterEnum;
import br.com.internet.bank.router.TransactionRouter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class TransactionService {

    private final TransactionRouter transactionRouter;

    public void sacarDinheiro(PushMoneyEntity pushMoneyEntity, String user) {
        Object[] obj = {pushMoneyEntity, user};

        try (CamelContext ctx = new DefaultCamelContext()) {
            ctx.addRoutes(transactionRouter.sacarDinheiro());
            ctx.start();
            try (ProducerTemplate producerTemplate = ctx.createProducerTemplate()) {
                producerTemplate.
                        requestBody(TransactionRouterEnum.ROUTE_PUSH_MONEY.getName(), obj);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if (ex.getCause() != null) {
                Throwable cause = ex.getCause();
                if (cause instanceof HttpClientErrorException.BadRequest) {
                    throw (HttpClientErrorException.BadRequest) cause;
                }
                if (cause instanceof InvalidDataAccessApiUsageException) {
                    throw (InvalidDataAccessApiUsageException) cause;
                }
                if (cause instanceof NotFoundException) {
                    throw (NotFoundException) cause;
                }
            }

            throw new InternetBankException(ex.getMessage());
        }
    }


    public void depositarDinheiro(PullMoneyEntity pullMoneyEntity, String user) {
        Object[] obj = {pullMoneyEntity, user};

        try (CamelContext ctx = new DefaultCamelContext()) {
            ctx.addRoutes(transactionRouter.depositarDinheiro());
            ctx.start();
            try (ProducerTemplate producerTemplate = ctx.createProducerTemplate()) {
                producerTemplate.
                        requestBody(TransactionRouterEnum.ROUTE_PULL_MONEY.getName(), obj);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if (ex.getCause() != null) {
                Throwable cause = ex.getCause();
                if (cause instanceof HttpClientErrorException.BadRequest) {
                    throw (HttpClientErrorException.BadRequest) cause;
                }
                if (cause instanceof InvalidDataAccessApiUsageException) {
                    throw (InvalidDataAccessApiUsageException) cause;
                }
            }

            throw new InternetBankException(ex.getMessage());
        }
    }

    @SuppressWarnings("rawtypes")
    public Page listarTransacaoPorData(
            Integer pageNo,
            Integer pageSize,
            List<String> sortBy, LocalDate dtCreated) {

        Object[] object = {pageNo, pageSize, sortBy, dtCreated};

        try (CamelContext ctx = new DefaultCamelContext()) {
            ctx.addRoutes(transactionRouter.listTransacaoPorData());
            ctx.start();
            try (ProducerTemplate producerTemplate = ctx.createProducerTemplate()) {
                return producerTemplate.
                        requestBody(TransactionRouterEnum.ROUTE_LIST_TRANSACTION_BY_DATA.getName(), object, Page.class);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if (ex.getCause() != null) {
                Throwable cause = ex.getCause();
                if (cause instanceof HttpClientErrorException.BadRequest) {
                    throw (HttpClientErrorException.BadRequest) cause;
                }
            }

            throw new InternetBankException(ex.getMessage());
        }
    }


    @SuppressWarnings("rawtypes")
    public Page listTransacaoDepositarDinheiroPorNumValor(
            Integer pageNo,
            Integer pageSize,
            List<String> sortBy, BigDecimal numValor) {

        Object[] object = {pageNo, pageSize, sortBy, numValor};

        try (CamelContext ctx = new DefaultCamelContext()) {
            ctx.addRoutes(transactionRouter.listTransacaoDepositarDinheiroPorNumValor());
            ctx.start();
            try (ProducerTemplate producerTemplate = ctx.createProducerTemplate()) {
                return producerTemplate.
                        requestBody(TransactionRouterEnum.ROUTE_LIST_TRANSACTION_PULL_MONEY_BY_NUM_VALUE.getName(), object, Page.class);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if (ex.getCause() != null) {
                Throwable cause = ex.getCause();
                if (cause instanceof HttpClientErrorException.BadRequest) {
                    throw (HttpClientErrorException.BadRequest) cause;
                }
            }

            throw new InternetBankException(ex.getMessage());
        }
    }


    public Page listTransacaoSaqueDinheiroPorNumValor(
            Integer pageNo,
            Integer pageSize,
            List<String> sortBy, BigDecimal numValor) {

        Object[] object = {pageNo, pageSize, sortBy, numValor};

        try (CamelContext ctx = new DefaultCamelContext()) {
            ctx.addRoutes(transactionRouter.listTransacaoSaqueDinheiroPorNumValor());
            ctx.start();
            try (ProducerTemplate producerTemplate = ctx.createProducerTemplate()) {
                return producerTemplate.
                        requestBody(TransactionRouterEnum.ROUTE_LIST_TRANSACTION_PUSH_MONEY_BY_NUM_VALUE.getName(), object, Page.class);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if (ex.getCause() != null) {
                Throwable cause = ex.getCause();
                if (cause instanceof HttpClientErrorException.BadRequest) {
                    throw (HttpClientErrorException.BadRequest) cause;
                }
            }

            throw new InternetBankException(ex.getMessage());
        }
    }


}