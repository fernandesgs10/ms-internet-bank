package br.com.internet.bank.controller;


import br.com.internet.bank.api.Cliente;
import br.com.internet.bank.api.ClienteApi;
import br.com.internet.bank.entity.ClientEntity;
import br.com.internet.bank.generate.Page;
import br.com.internet.bank.mapper.ClientMapper;
import br.com.internet.bank.service.ClientService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("ALL")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("${openapi.ms-internet-bank.base-path:/v1/internet-bank}/cliente")
@SecurityRequirement(name = "in-memory")
public class ClientController extends Page implements ClienteApi {

    private final ClientMapper clientMapper;
    private final ClientService clientService;

    @Override
    @PostMapping(value = "/criar-cliente")
    public ResponseEntity<Void> criarCliente(
            @ApiParam(value = "Optional description in new client", required = true)
            @Valid @RequestBody Cliente cliente) {
        log.info("class=ClientController method=criarCliente step=cliente={}", cliente);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        BigDecimal balance = cliente.getSaldo();

        ClientEntity clientEntity =
                clientMapper.converterObjectToClientEntity(cliente);

        clientService.createClient(clientEntity, balance, user.getUsername());

        log.info("class=ClientController method=criarCliente step=end response");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @GetMapping
    public ResponseEntity<br.com.internet.bank.api.Page> listarCliente(
            @ApiParam(value = "The number of items to skip before starting to collect the result set.", defaultValue = "0")
            @Valid @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
            @ApiParam(value = "The number of items to return.", defaultValue = "20")
            @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @Valid @RequestParam(value = "sortBy", required = false) List<String> sortBy) {

        log.info("class=ClientController method=listarCliente step=pageNo={}, pageSize={}, sortBy={}",
                pageNo, pageSize, sortBy);

        org.springframework.data.domain.Page<ClientEntity> page = clientService.listClient(pageNo, pageSize, sortBy);

        org.springframework.data.domain.Page<Cliente> pageClientDto = convertPage(page,
                clientMapper::converterObjectToClient);

        br.com.internet.bank.api.Page pageConvert =
                clientMapper.converterToPageClient(pageClientDto);

        pageConvert.content(Collections.singletonList(pageClientDto.getContent()));

        log.info("class=ClientController method=listarCliente step=end response{}", pageConvert);
        return ResponseEntity.status(HttpStatus.OK).body(pageConvert);
    }
}
