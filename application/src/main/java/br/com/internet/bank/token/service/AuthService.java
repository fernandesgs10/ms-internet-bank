package br.com.internet.bank.token.service;

import br.com.internet.bank.token.dto.AuthRequestDto;

import java.util.Map;

public interface AuthService {
     Map<String, String> authRequest(AuthRequestDto authRequestDto);

}
