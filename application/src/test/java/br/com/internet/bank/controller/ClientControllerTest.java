package br.com.internet.bank.controller;

import br.com.internet.bank.api.Cliente;
import br.com.internet.bank.api.Pageable;
import br.com.internet.bank.api.Sort;
import br.com.internet.bank.entity.ClientEntity;
import br.com.internet.bank.mapper.ClientMapper;
import br.com.internet.bank.service.ClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@DisplayName("Teste integration endpoints client")
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = ClientController.class)
@Import(ClientController.class)
public class ClientControllerTest {

    public static final String ENDPOINT_CRUD_CONTROLLER = "/v1/internet-bank/cliente";
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ClientMapper clientMapper;
    @MockBean
    ClientService clientService;


    @Test
    @DisplayName("Endpoint to Client list")
    public void listAddress_whenDo_expectedResult() throws Exception {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setCoSeqCliente(1L);

        Cliente cliente = new Cliente();
        cliente.setCoSeqCliente(new BigDecimal(1));
        cliente.setNome("Test");
        cliente.setPlanoExclusive(Boolean.TRUE);
        cliente.setSaldo(BigDecimal.valueOf(10));
        cliente.setNumeroConta("010101");
        cliente.setDataNascimento(LocalDate.of(1979, 2, 5));
        cliente.setDtCreated(LocalDateTime.now().toString());

        Sort sort = new Sort();
        sort.setSorted(Boolean.FALSE);
        sort.unsorted(Boolean.TRUE);
        sort.setEmpty(new BigDecimal(1));

        br.com.internet.bank.api.Page pageReturn = new br.com.internet.bank.api.Page();
        pageReturn.setSize(new BigDecimal(20));
        pageReturn.setTotalPages(new BigDecimal(1));
        pageReturn.setFirst(Boolean.TRUE);
        pageReturn.setLast(Boolean.TRUE);
        pageReturn.setNumber(new BigDecimal(0));
        pageReturn.setTotalElements(new BigDecimal(1));
        pageReturn.setNumberOfElements(new BigDecimal(1));
        pageReturn.setSort(sort);

        Pageable pageable = new Pageable();
        pageable.setPageNumber(new BigDecimal(0));
        pageable.setPageSize(new BigDecimal(20));
        pageable.setOffset(new BigDecimal(0));
        pageable.setPaged(Boolean.TRUE);
        pageable.setUnpaged(Boolean.FALSE);
        pageable.setSort(sort);

        pageReturn.setPageable(pageable);

        pageReturn.content(List.of(clientEntity));

        Page<ClientEntity> page = new PageImpl<>(List.of(clientEntity));

        when(clientService.listClient(any(), any(), any())).thenReturn(page);
        when(clientMapper.converterObjectToClient(any())).thenReturn(cliente);

        when(clientMapper.converterToPageClient(any())).thenReturn(pageReturn);

        ResultActions result = mockMvc
                .perform(MockMvcRequestBuilders
                .get(ENDPOINT_CRUD_CONTROLLER )
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        String content = result.andReturn().getResponse().getContentAsString();
        Assert.notNull(content);
    }
}
