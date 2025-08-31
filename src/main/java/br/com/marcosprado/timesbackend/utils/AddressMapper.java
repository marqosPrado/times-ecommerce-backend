package br.com.marcosprado.timesbackend.utils;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import br.com.marcosprado.timesbackend.domain.Address;

import java.util.List;
import java.util.stream.Collectors;

public class AddressMapper {

    public static AddressAggregate toAggregate(Address address) {
        return new AddressAggregate(
                address.getTypeResidence(),
                address.getTypePlace(),
                address.getStreet(),
                address.getNumber(),
                address.getNeighborhood(),
                address.getCep(),
                address.getCountry(),
                address.getObservation(),
                StateMapper.toAggregate(address.getState())
        );
    }

    public static List<AddressAggregate> toAggregate(List<Address> addresses) {
        return addresses.stream()
                .map(AddressMapper::toAggregate)
                .collect(Collectors.toList());
    }
}
