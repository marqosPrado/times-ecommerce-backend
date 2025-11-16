package br.com.marcosprado.timesbackend.service.ia;

import br.com.marcosprado.timesbackend.dto.chat.ConversationMessage;
import com.google.genai.Client;
import com.google.genai.types.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeminiChatService implements IAChat {
    private final String systemInstruction;

    public GeminiChatService() {
        this.systemInstruction = this.getSystemInstruction();
    }

    private String getSystemInstruction() {
        try {
            ClassPathResource resource = new ClassPathResource("chat-instructions.txt");
            InputStream inputStream = resource.getInputStream();

            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao carregar instruções do chatbot", e);
        }
    }

    @Override
    public String sendMessage(String message, List<ConversationMessage> conversationHistory) {
        Client client = new Client();

        List<Content> contents = new ArrayList<>();

        if (conversationHistory != null && !conversationHistory.isEmpty()) {
            for (ConversationMessage msg : conversationHistory) {
                contents.add(createContent(msg.role(), msg.content()));
            }
        }

        contents.add(createContent("user", message));

        GenerateContentConfig config = GenerateContentConfig.builder()
                .systemInstruction(Content.fromParts(Part.fromText(systemInstruction)))
                .build();

        try {
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    contents,
                    config
            );

            String textResponse = response.text();
            return textResponse != null ? textResponse.trim() : "Desculpe, não consegui processar sua mensagem. Por favor, tente novamente.";
        } catch (Exception e) {
            return "Desculpe, ocorreu um erro ao processar sua mensagem. Por favor, tente novamente.";
        }
    }

    private Content createContent(String role, String text) {
        String geminiRole = role.equals("assistant") ? "model" : role;

        return Content.builder()
                .role(geminiRole)
                .parts(List.of(Part.fromText(text)))
                .build();
    }
}
