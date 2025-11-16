package br.com.marcosprado.timesbackend.dto.chat;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Request DTO for chat messages sent by the user
 */
public record ChatMessageRequest(
        @NotBlank(message = "Message cannot be blank")
        String message,

        /**
         * Optional conversation ID for maintaining context
         * If null, starts a new conversation
         */
        String conversationId,

        /**
         * Optional conversation history for stateless chatbot
         * Each entry should contain role (user/assistant) and content
         */
        List<ConversationMessage> conversationHistory
) {
}
