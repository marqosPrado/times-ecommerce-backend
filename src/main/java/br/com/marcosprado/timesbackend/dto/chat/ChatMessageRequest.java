package br.com.marcosprado.timesbackend.dto.chat;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ChatMessageRequest(
        @NotBlank(message = "Message cannot be blank")
        String message,

        String conversationId,

        List<ConversationMessage> conversationHistory
) {
}
