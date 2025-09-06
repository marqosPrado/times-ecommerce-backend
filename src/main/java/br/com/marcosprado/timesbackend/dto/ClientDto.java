package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.enums.TypePhoneNumber;

import java.time.LocalDate;
import java.util.List;

public record ClientDto(
        String fullName,
        LocalDate birthDate,
        String cpf,
        String email,
        String password,
        TypePhoneNumber typePhoneNumber,
        String phoneNumber,
        List<AddressDto> addresses,
        List<CreditCardDto> creditCards
) {
}
