package br.com.internet.bank.exchange;

import br.com.internet.bank.entity.ClientEntity;
import org.apache.camel.Exchange;
import org.springframework.data.domain.Page;

public interface ClientExchange {

	void createClient(Exchange exchange);

	Page<ClientEntity> listClient(Exchange exchange);

}
