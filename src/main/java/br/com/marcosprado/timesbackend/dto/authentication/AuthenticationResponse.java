package br.com.marcosprado.timesbackend.dto.authentication;

import br.com.marcosprado.timesbackend.dto.client.response.UserInfoResponse;

public record AuthenticationResponse(
        String token,
        String refreshToken,
        String type,
        UserInfoResponse user
) {
    public AuthenticationResponse(String token, String refreshToken, UserInfoResponse user) {
        this(token, refreshToken, "Bearer", user);
    }
}
