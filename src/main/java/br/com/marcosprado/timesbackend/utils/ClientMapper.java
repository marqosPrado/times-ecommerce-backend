package br.com.marcosprado.timesbackend.utils;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;
import br.com.marcosprado.timesbackend.domain.Client;

public class ClientMapper {

    public static ClientAggregate toAggregate(Client client) {
        return new ClientAggregate(
                client.getFullName(),
                client.getBirthDate(),
                client.getCpf(),
                client.getEmail(),
                client.getPassword(),
                client.getTypePhoneNumber(),
                client.getPhoneNumber(),
                client.isActive(),
                client.getCredit(),
                AddressMapper.toAggregate(client.getAddresses()),
                CreditCardMapper.toAggregate(client.getCreditCards())
        );
    }
}
