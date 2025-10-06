package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import br.com.marcosprado.timesbackend.aggregate.StateAggregate;
import br.com.marcosprado.timesbackend.enums.TypePlace;
import br.com.marcosprado.timesbackend.enums.TypeResidence;

import java.util.Collection;

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

    public static AddressDto[] fromEntities(Collection<AddressAggregate> addresses) {
        return addresses.stream()
                .map(AddressDto::fromEntity)
                .toArray(AddressDto[]::new);
    }

    public AddressAggregate toEntity(StateAggregate state) {
        return new AddressAggregate(
                this.typeResidence,
                this.typePlace,
                this.street,
                this.number,
                this.neighborhood,
                this.cep,
                this.city,
                this.country,
                this.observations,
                state
        );
    }
}
