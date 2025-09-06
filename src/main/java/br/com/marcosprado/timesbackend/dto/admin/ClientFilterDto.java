package br.com.marcosprado.timesbackend.dto.admin;

import br.com.marcosprado.timesbackend.enums.Gender;

public record ClientFilterDto(
        String name,
        String cpf,
        Gender gender,
        String email,
        String phoneNumber,
        Boolean active
) {
}
