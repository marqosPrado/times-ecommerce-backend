package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.enums.TypePlace;
import br.com.marcosprado.timesbackend.enums.TypeResidence;

public record AddressDto(
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
}
