package br.com.internet.bank.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClientRouterEnum {

    ROUTE_LIST_CLIENT("direct:listClient"),

    ROUTE_CREATE_CLIENT("direct:createClient");

    private final String name;



}
