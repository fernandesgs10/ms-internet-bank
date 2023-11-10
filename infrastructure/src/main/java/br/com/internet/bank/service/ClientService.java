package br.com.internet.bank.service;

import br.com.internet.bank.common.InternetBankException;
import br.com.internet.bank.entity.ClientEntity;
import br.com.internet.bank.enums.ClientRouterEnum;
import br.com.internet.bank.router.ClientRouter;
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
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ClientService {

    private final ClientRouter clientRouter;

    public void createClient(ClientEntity clientEntity, BigDecimal balance, String user) {
        Object[] obj = {clientEntity, balance, user};

        try (CamelContext ctx = new DefaultCamelContext()) {
            ctx.addRoutes(clientRouter.createClient());
            ctx.start();
            try (ProducerTemplate producerTemplate = ctx.createProducerTemplate()) {
                producerTemplate.
                        requestBody(ClientRouterEnum.ROUTE_CREATE_CLIENT.getName(), obj);
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
    public Page listClient(
                                 Integer pageNo,
                                 Integer pageSize,
                                 List<String> sortBy) {

        Object[] object = {pageNo, pageSize, sortBy};

        try (CamelContext ctx = new DefaultCamelContext()) {
            ctx.addRoutes(clientRouter.listClient());
            ctx.start();
            try (ProducerTemplate producerTemplate = ctx.createProducerTemplate()) {
                return producerTemplate.
                        requestBody(ClientRouterEnum.ROUTE_LIST_CLIENT.getName(), object, Page.class);
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