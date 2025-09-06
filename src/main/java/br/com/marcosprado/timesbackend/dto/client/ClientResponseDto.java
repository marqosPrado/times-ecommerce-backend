package br.com.marcosprado.timesbackend.dto.client;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;

public record ClientResponseDto(
        Integer id,
        String fullName,
        String cpf,
        String email,
        String phoneNumber,
        Boolean active
) {
    public static ClientResponseDto from(ClientAggregate client) {
        return new ClientResponseDto(
                client.getId(),
                client.getFullName(),
                client.getCpf(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getActive()
        );
    }
}
