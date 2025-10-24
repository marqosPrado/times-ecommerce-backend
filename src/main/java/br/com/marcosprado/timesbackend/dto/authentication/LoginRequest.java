package br.com.marcosprado.timesbackend.dto.authentication;

public record LoginRequest(
        String email,
        String password
) {
}
