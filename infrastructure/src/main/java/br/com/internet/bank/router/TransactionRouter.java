package br.com.internet.bank.router;

import br.com.internet.bank.enums.TransactionRouterEnum;
import br.com.internet.bank.exchange.TransactionExchange;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionRouter {

    private final TransactionExchange transactionExchange;

    public RouteBuilder sacarDinheiro() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(TransactionRouterEnum.ROUTE_PUSH_MONEY.getName())
                        .bean(transactionExchange, "sacarDinheiro");
            }
        };
    }

    public RouteBuilder depositarDinheiro() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(TransactionRouterEnum.ROUTE_PULL_MONEY.getName())
                        .bean(transactionExchange, "depositarDinheiro");
            }
        };
    }

    public RouteBuilder listTransacaoPorData() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(TransactionRouterEnum.ROUTE_LIST_TRANSACTION_BY_DATA.getName())
                        .bean(transactionExchange, "listarTransacaoPorData");
            }
        };
    }

    public RouteBuilder listTransacaoDepositarDinheiroPorNumValor() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(TransactionRouterEnum.ROUTE_LIST_TRANSACTION_PULL_MONEY_BY_NUM_VALUE.getName())
                        .bean(transactionExchange, "listTransacaoDepositarDinheiroPorNumValor");
            }
        };
    }

    public RouteBuilder listTransacaoSaqueDinheiroPorNumValor() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(TransactionRouterEnum.ROUTE_LIST_TRANSACTION_PUSH_MONEY_BY_NUM_VALUE.getName())
                        .bean(transactionExchange, "listTransacaoSaqueDinheiroPorNumValor");
            }
        };
    }

}