package br.com.marcosprado.timesbackend.service.ia;

import br.com.marcosprado.timesbackend.dto.chat.ConversationMessage;

import java.util.List;

/**
 * Interface for AI-powered chat services
 */
public interface IAChat {
    /**
     * Send a message to the AI chatbot and get a response
     *
     * @param message The user's message
     * @param conversationHistory Optional conversation history for context
     * @return The AI's response
     */
    String sendMessage(String message, List<ConversationMessage> conversationHistory);
}
