package br.com.internet.bank;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SecurityScheme(
        name = "in-memory",
        type = SecuritySchemeType.APIKEY,
        bearerFormat = "JWT",
        scheme = "Bearer",
        paramName = "Authorization",
        in = SecuritySchemeIn.HEADER
)
@Slf4j
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }




}