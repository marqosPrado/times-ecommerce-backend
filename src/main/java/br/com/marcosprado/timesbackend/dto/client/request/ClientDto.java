package br.com.marcosprado.timesbackend.dto.client.request;

import br.com.marcosprado.timesbackend.dto.AddressDto;
import br.com.marcosprado.timesbackend.dto.CreditCardDto;
import br.com.marcosprado.timesbackend.enums.Gender;
import br.com.marcosprado.timesbackend.enums.TypePhoneNumber;

import java.time.LocalDate;
import java.util.List;

public record ClientDto(
        String fullName,
        LocalDate birthDate,
        String cpf,
        Gender gender,
        String email,
        String password,
        TypePhoneNumber typePhoneNumber,
        String phoneNumber,
        List<AddressDto> addresses,
        List<CreditCardDto> creditCards
) {
}
