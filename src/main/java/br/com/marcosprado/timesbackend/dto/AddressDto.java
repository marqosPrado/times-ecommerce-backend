package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import br.com.marcosprado.timesbackend.enums.TypePlace;
import br.com.marcosprado.timesbackend.enums.TypeResidence;

public record AddressDto(
        Integer id,
        TypeResidence typeResidence,
        TypePlace typePlace,
        String street,
        Integer number,
        String neighborhood,
        String cep,
        String city,
        String country,
        String observations,
        Integer stateId
) {
    public static AddressDto fromEntity(AddressAggregate address) {
        return new AddressDto(
                address.getId(),
                address.getTypeResidence(),
                address.getTypePlace(),
                address.getStreet(),
                address.getNumber(),
                address.getNeighborhood(),
                address.getCep(),
                address.getCity(),
                address.getCountry(),
                address.getObservations(),
                address.getState().getId()
        );
    }
}
