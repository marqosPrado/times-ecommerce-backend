package br.com.marcosprado.timesbackend.service.ia;

import br.com.marcosprado.timesbackend.dto.chat.ConversationMessage;

import java.util.List;

public interface IAChat {
    String sendMessage(String message, List<ConversationMessage> conversationHistory);
}
