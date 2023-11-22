package br.com.internet.bank.controller;

import br.com.internet.bank.MainApplication;
import br.com.internet.bank.token.dto.AuthRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest(classes = MainApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class SpringIntegrationTest {
    static ResponseResults latestResponse = null;

    protected RestTemplate restTemplate;

    @LocalServerPort
    protected int port;

    @Value("${jwt.user}")
    private String user;

    @Value("${jwt.password}")
    private String password;


    public SpringIntegrationTest() {
        restTemplate = new RestTemplate();
    }

    @SneakyThrows
    private Token executeToken() {
        AuthRequestDto authRequestDto = new AuthRequestDto(user, password);
        ObjectMapper mapper = new ObjectMapper();
        String authRequestJson = mapper.writeValueAsString(authRequestDto);

        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);

        String url = "http://localhost:" + port + "/v1/internet-bank/auth/login";
        HttpEntity<String> entity = new HttpEntity<>(authRequestJson, headers);
        ResponseEntity<Token> response = restTemplate.exchange(url, HttpMethod.POST, entity, Token.class);

        log.info("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
        log.info("Response =" + response.getBody());

        return response.getBody();
    }

    void executeGet(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", "Bearer " + executeToken().getToken());
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);

        latestResponse = restTemplate.execute(url, HttpMethod.GET, requestCallback, response -> {
            if (errorHandler.hadError) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });
    }


    private static class ResponseResultErrorHandler implements ResponseErrorHandler {
        private ResponseResults results = null;
        private Boolean hadError = false;

        private ResponseResults getResults() {
            return results;
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            hadError = response.getRawStatusCode() >= 400;
            return hadError;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            results = new ResponseResults(response);
        }
    }

    @Data
    private static class Token {
        private String token;
    }
}