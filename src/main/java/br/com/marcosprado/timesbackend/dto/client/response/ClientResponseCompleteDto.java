package br.com.marcosprado.timesbackend.dto.client.response;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.dto.AddressDto;
import br.com.marcosprado.timesbackend.dto.CreditCardDto;
import br.com.marcosprado.timesbackend.enums.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record ClientResponseCompleteDto(
        String fullName,
        String phoneNumber,
        Gender gender,
        String email,
        String cpf,
        LocalDate birthDate,
        List<AddressDto> addresses,
        List<CreditCardDto> creditCards
) {
    public static ClientResponseCompleteDto fromEntity(ClientAggregate client) {
        List<AddressDto> addresses = client.getAddresses() != null
                ? client.getAddresses().stream()
                .map(AddressDto::fromEntity)
                .collect(Collectors.toList())
                : List.of();

        List<CreditCardDto> cards = client.getCreditCards() != null
                ? client.getCreditCards().stream()
                .map(CreditCardDto::fromEntity)
                .collect(Collectors.toList())
                : List.of();

        return new ClientResponseCompleteDto(
                client.getFullName(),
                client.getPhoneNumber(),
                client.getGender(),
                client.getEmail(),
                client.getCpf(),
                client.getBirthDate(),
                addresses,
                cards
        );
    }
}
