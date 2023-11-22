package br.com.internet.bank.controller;

import br.com.internet.bank.dto.VersionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StepDefsIntegrationTest extends SpringIntegrationTest {

    private final ObjectMapper mapper;

    public StepDefsIntegrationTest() {
        mapper = new ObjectMapper();
    }

    @When("^the client calls /version$")
    public void the_client_issues_GET_version() {
        executeGet("http://localhost:" + port + "/v1/internet-bank/version");
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        HttpStatusCode statusCodeReturn = latestResponse.getTheResponse().getStatusCode();
        final HttpStatus currentStatusCode = (HttpStatus) statusCodeReturn;
        VersionDTO versionDTO = mapper.readValue(latestResponse.getBody(), VersionDTO.class);
        assertThat("status code is incorrect : " + versionDTO.getVersion(), currentStatusCode.value(), is(statusCode));
    }

    @And("^the client receives server version (.+)$")
    public void the_client_receives_server_version_body(String version) throws Throwable {
        VersionDTO versionDTO = mapper.readValue(latestResponse.getBody(), VersionDTO.class);
        assertThat(versionDTO.getVersion(), is(version));
    }
}