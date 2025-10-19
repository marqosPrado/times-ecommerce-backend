package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ShippingAddressResponse(
        Integer id,

        @JsonProperty("type_residence")
        String typeResidence,

        @JsonProperty("type_place")
        String typePlace,

        @JsonProperty("street")
        String street,

        @JsonProperty("number")
        Integer number,

        @JsonProperty("neighborhood")
        String neighborhood,

        @JsonProperty("cep")
        String cep,

        @JsonProperty("city")
        String city,

        @JsonProperty("state")
        String state,

        @JsonProperty("country")
        String country,

        @JsonProperty("observations")
        String observations
) {

    public static ShippingAddressResponse fromEntity(AddressAggregate address) {
        if (address == null) return null;

        return new ShippingAddressResponse(
                address.getId(),
                address.getTypeResidence().name(),
                address.getTypePlace().name(),
                address.getStreet(),
                address.getNumber(),
                address.getNeighborhood(),
                address.getCep(),
                address.getCity(),
                address.getState().getState(),
                address.getCountry(),
                address.getObservations()
        );
    }
}
