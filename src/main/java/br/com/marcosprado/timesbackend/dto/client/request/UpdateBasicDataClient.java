package br.com.marcosprado.timesbackend.dto.client.request;

import br.com.marcosprado.timesbackend.enums.Gender;

public record UpdateBasicDataClient(
        String fullName,
        String phoneNumber,
        Gender gender
) {
}
