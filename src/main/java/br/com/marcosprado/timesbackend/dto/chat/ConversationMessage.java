package br.com.marcosprado.timesbackend.dto.chat;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a single message in a conversation
 */
public record ConversationMessage(
        @NotBlank(message = "Role cannot be blank")
        String role, // "user" or "assistant"

        @NotBlank(message = "Content cannot be blank")
        String content
) {
}
