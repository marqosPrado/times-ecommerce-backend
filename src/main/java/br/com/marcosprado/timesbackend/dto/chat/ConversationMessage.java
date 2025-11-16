package br.com.marcosprado.timesbackend.dto.chat;

import jakarta.validation.constraints.NotBlank;

public record ConversationMessage(
        @NotBlank(message = "Role cannot be blank")
        String role,

        @NotBlank(message = "Content cannot be blank")
        String content
) {
}
