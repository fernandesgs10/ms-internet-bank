package br.com.internet.bank.router;

import br.com.internet.bank.enums.ClientRouterEnum;
import br.com.internet.bank.exchange.ClientExchange;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientRouter {

    private final ClientExchange clientExchange;


    public RouteBuilder createClient() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ClientRouterEnum.ROUTE_CREATE_CLIENT.getName())
                        .bean(clientExchange, "createClient");
            }
        };
    }

    public RouteBuilder listClient() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ClientRouterEnum.ROUTE_LIST_CLIENT.getName())
                        .bean(clientExchange, "listClient");
            }
        };
    }
}