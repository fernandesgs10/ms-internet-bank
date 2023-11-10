package br.com.internet.bank.dto;

import br.com.internet.bank.entity.ClientEntity;
import br.com.internet.bank.entity.PullMoneyEntity;
import br.com.internet.bank.entity.PushMoneyEntity;
import lombok.Data;

import java.util.List;

@Data
public class ExtractDTO {

    private ClientEntity client;
    private List<PushMoneyEntity> saqueDinheiro;
    private List<PullMoneyEntity> depositarDinheiro;
}
