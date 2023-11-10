package br.com.internet.bank.token.dto;

import java.util.List;

public record User(String username, String password, List<String> roles) {
}