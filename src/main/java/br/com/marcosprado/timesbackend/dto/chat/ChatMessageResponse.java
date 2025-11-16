package br.com.marcosprado.timesbackend.dto.chat;


import br.com.marcosprado.timesbackend.dto.product.response.ProductFilterResponse;

import java.util.List;

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
