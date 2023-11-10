package br.com.internet.bank.mapper;

import br.com.internet.bank.api.Cliente;
import br.com.internet.bank.api.Transaction;
import br.com.internet.bank.entity.ClientEntity;
import br.com.internet.bank.entity.PullMoneyEntity;
import br.com.internet.bank.entity.PushMoneyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Builder
@Component
@AllArgsConstructor
public class TransactionMapper implements Serializable {

    private final ModelMapper modelMapper;

    public Transaction converterObjectToTransaction(Object object) {
        return modelMapper.map(object, Transaction.class);
    }

    public br.com.internet.bank.api.Page converterToPageTransaction(Object obj) {
        return modelMapper.map(obj, br.com.internet.bank.api.Page.class);
    }

    public PullMoneyEntity converterObjectToPullMoneyEntity(Object object) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Cliente, ClientEntity>() {
            @Override
            protected void configure() {
                skip(destination.getDtCreated());
                skip(destination.getLastUpdated());
            }
        });

        return modelMapper.map(object, PullMoneyEntity.class);
    }

    public PushMoneyEntity converterObjectToPushMoneyEntity(Object object) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Cliente, ClientEntity>() {
            @Override
            protected void configure() {
                    skip(destination.getDtCreated());
                    skip(destination.getLastUpdated());
            }
        });

        return modelMapper.map(object, PushMoneyEntity.class);
    }

}