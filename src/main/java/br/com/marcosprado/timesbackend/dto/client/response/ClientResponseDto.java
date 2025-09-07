package br.com.marcosprado.timesbackend.dto.client.response;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;
import br.com.marcosprado.timesbackend.enums.Gender;

public record ClientResponseDto(
        Integer id,
        String fullName,
        String cpf,
        String email,
        String phoneNumber,
        Boolean active,
        Gender gender
) {
    public static ClientResponseDto from(ClientAggregate client) {
        return new ClientResponseDto(
                client.getId(),
                client.getFullName(),
                client.getCpf(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getActive(),
                client.getGender()
        );
    }
}
