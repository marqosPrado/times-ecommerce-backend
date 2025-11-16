package br.com.marcosprado.timesbackend.dto.chat;

import br.com.marcosprado.timesbackend.dto.product.ProductFilterResponse;

import java.util.List;

/**
 * Response DTO containing chatbot's reply and optional product suggestions
 */
public record ChatMessageResponse(
        String message,
        String conversationId,
        List<ProductFilterResponse> suggestedProducts,
        boolean hasProductSuggestions
) {
    public ChatMessageResponse(String message, String conversationId) {
        this(message, conversationId, null, false);
    }

    public ChatMessageResponse(String message, String conversationId, List<ProductFilterResponse> suggestedProducts) {
        this(message, conversationId, suggestedProducts, suggestedProducts != null && !suggestedProducts.isEmpty());
    }
}
