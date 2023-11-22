package br.com.internet.bank.token.controller;


import br.com.internet.bank.config.MessageResourceConfig;
import br.com.internet.bank.token.dto.AuthRequestDto;
import br.com.internet.bank.token.service.AuthService;
import jakarta.servlet.ServletContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Preconditions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/internet-bank/auth")
@RequiredArgsConstructor
@Data
public class AuthController {

    @Value("${jwt.user}")
    private String user;

    @Value("${jwt.password}")
    private String password;

    private final MessageResourceConfig messageResourceConfig;

    @Resource
    private ServletContext context;

    private final AuthService authService;
    @SuppressWarnings("unused")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authRequest(@RequestBody AuthRequestDto authRequestDto) {
        log.info("AuthResource.authRequest start {}", authRequestDto);

        context.setAttribute("userName", authRequestDto.userName());
        context.setAttribute("password", authRequestDto.password());

        boolean isUser = user.equals(authRequestDto.userName()) && password.equals(authRequestDto.password());
        Preconditions.checkArgument(isUser, messageResourceConfig.getMessage("user.notfound",
                authRequestDto.userName(), authRequestDto.password()));

        var userRegistrationResponse = authService.authRequest(authRequestDto);
        log.info("AuthResource.authRequest end {}", userRegistrationResponse);
        return new ResponseEntity<>(userRegistrationResponse, HttpStatus.OK);
    }
}
