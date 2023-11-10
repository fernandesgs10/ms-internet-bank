package br.com.internet.bank.exchange;

import br.com.internet.bank.dto.ExtractDTO;
import org.apache.camel.Exchange;
import org.springframework.data.domain.Page;

@SuppressWarnings("unused")
public interface TransactionExchange {

	void sacarDinheiro(Exchange exchange);

	void depositarDinheiro(Exchange exchange);

	Page<ExtractDTO> listTransacaoDepositarDinheiroPorNumValor(Exchange exchange);

	public Page<ExtractDTO> listTransacaoSaqueDinheiroPorNumValor(Exchange exchange);

	Page<ExtractDTO> listarTransacaoPorData(Exchange exchange);

}
