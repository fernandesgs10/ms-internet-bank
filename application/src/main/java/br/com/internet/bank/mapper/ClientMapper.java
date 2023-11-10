package br.com.internet.bank.mapper;

import br.com.internet.bank.api.Cliente;
import br.com.internet.bank.entity.ClientEntity;
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
public class ClientMapper implements Serializable {

    private final ModelMapper modelMapper;

    public Cliente converterObjectToClient(Object object) {
        return modelMapper.map(object, Cliente.class);
    }

    public ClientEntity converterObjectToClientEntity(Object object) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Cliente, ClientEntity>() {
            @Override
            protected void configure() {
                    skip(destination.getDtCreated());
                    skip(destination.getLastUpdated());
            }
        });

        return modelMapper.map(object, ClientEntity.class);

    }
    public br.com.internet.bank.api.Page converterToPageClient(Object obj) {
        return modelMapper.map(obj, br.com.internet.bank.api.Page.class);
    }

}