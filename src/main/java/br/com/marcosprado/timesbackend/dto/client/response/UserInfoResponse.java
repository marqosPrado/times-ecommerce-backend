package br.com.marcosprado.timesbackend.dto.client.response;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;
import br.com.marcosprado.timesbackend.enums.Gender;

public record UserInfoResponse(
        Integer id,
        String fullName,
        String cpf,
        String email,
        String phoneNumber,
        Boolean active,
        Gender gender,
        String role
) {
    public static UserInfoResponse from(ClientAggregate client) {
        return new UserInfoResponse(
                client.getId(),
                client.getFullName(),
                client.getCpf(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getActive(),
                client.getGender(),
                client.getRole().name()
        );
    }
}
