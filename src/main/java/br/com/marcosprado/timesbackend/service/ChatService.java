package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.Product;
import br.com.marcosprado.timesbackend.aggregate.enumeration.Gender;
import br.com.marcosprado.timesbackend.dto.chat.ChatMessageRequest;
import br.com.marcosprado.timesbackend.dto.chat.ChatMessageResponse;
import br.com.marcosprado.timesbackend.dto.chat.ConversationMessage;
import br.com.marcosprado.timesbackend.dto.product.response.ProductFilterResponse;
import br.com.marcosprado.timesbackend.repository.ProductRepository;
import br.com.marcosprado.timesbackend.service.ia.IAChat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for handling chatbot conversations and product searches
 */
@Service
public class ChatService {

    private final IAChat geminiChatService;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ChatService(IAChat geminiChatService, ProductRepository productRepository) {
        this.geminiChatService = geminiChatService;
        this.productRepository = productRepository;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Process a chat message and return a response with optional product suggestions
     *
     * @param request The chat message request
     * @return ChatMessageResponse with AI response and optional product suggestions
     */
    public ChatMessageResponse processMessage(ChatMessageRequest request) {
        // Get AI response
        String aiResponse = geminiChatService.sendMessage(
                request.message(),
                request.conversationHistory()
        );

        // Generate or use existing conversation ID
        String conversationId = request.conversationId() != null
                ? request.conversationId()
                : UUID.randomUUID().toString();

        // Check if response contains product search criteria
        SearchCriteria searchCriteria = extractSearchCriteria(aiResponse);

        if (searchCriteria != null && searchCriteria.hasValidCriteria()) {
            // Remove search criteria JSON from the user-facing message
            String cleanedMessage = removeSearchCriteriaFromMessage(aiResponse);

            // Search for products
            List<ProductFilterResponse> products = searchProducts(searchCriteria);

            return new ChatMessageResponse(cleanedMessage, conversationId, products);
        }

        // No product search needed
        return new ChatMessageResponse(aiResponse, conversationId);
    }

    /**
     * Extract search criteria from AI response
     * Looks for CRITÉRIOS_BUSCA: {...} pattern
     */
    private SearchCriteria extractSearchCriteria(String aiResponse) {
        try {
            // Pattern to find CRITÉRIOS_BUSCA: { ... }
            Pattern pattern = Pattern.compile("CRITÉRIOS_BUSCA:\\s*\\{[^}]*\\}", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(aiResponse);

            if (matcher.find()) {
                String jsonStr = matcher.group();
                // Extract just the JSON part
                jsonStr = jsonStr.substring(jsonStr.indexOf("{"));

                JsonNode jsonNode = objectMapper.readTree(jsonStr);

                return new SearchCriteria(
                        getStringField(jsonNode, "gender"),
                        getStringField(jsonNode, "brand"),
                        getStringField(jsonNode, "style"),
                        getStringField(jsonNode, "mechanism"),
                        getStringField(jsonNode, "line"),
                        getStringField(jsonNode, "boxMaterial"),
                        getStringField(jsonNode, "dial"),
                        getStringField(jsonNode, "boxFormat")
                );
            }
        } catch (Exception e) {
            // If parsing fails, return null (no search criteria)
            return null;
        }

        return null;
    }

    /**
     * Remove search criteria JSON from the message to show only the conversational part
     */
    private String removeSearchCriteriaFromMessage(String message) {
        return message.replaceAll("CRITÉRIOS_BUSCA:\\s*\\{[^}]*\\}", "").trim();
    }

    /**
     * Helper method to safely extract string fields from JSON
     */
    private String getStringField(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull() || field.asText().isBlank()) {
            return null;
        }
        return field.asText();
    }

    /**
     * Search for products based on extracted criteria
     */
    private List<ProductFilterResponse> searchProducts(SearchCriteria criteria) {
        try {
            // Use the repository's partial characteristics method
            List<Product> products = productRepository.findByPartialCharacteristics(
                    criteria.brand,
                    criteria.style,
                    criteria.mechanism,
                    criteria.boxMaterial,
                    criteria.boxFormat,
                    criteria.dial,
                    criteria.line
            );

            // Filter by gender if specified
            if (criteria.gender != null) {
                try {
                    Gender gender = Gender.valueOf(criteria.gender.toUpperCase());
                    products = products.stream()
                            .filter(p -> p.getGender() == gender)
                            .toList();
                } catch (IllegalArgumentException e) {
                    // Invalid gender, ignore filter
                }
            }

            // Limit to 10 products
            List<Product> limitedProducts = products.stream()
                    .limit(10)
                    .toList();

            return limitedProducts.stream()
                    .map(ProductFilterResponse::fromEntity)
                    .toList();
        } catch (Exception e) {
            // If search fails, return empty list
            return List.of();
        }
    }

    /**
     * Internal class to hold search criteria extracted from AI response
     */
    private record SearchCriteria(
            String gender,
            String brand,
            String style,
            String mechanism,
            String line,
            String boxMaterial,
            String dial,
            String boxFormat
    ) {
        boolean hasValidCriteria() {
            return gender != null || brand != null || style != null ||
                    mechanism != null || line != null || boxMaterial != null ||
                    dial != null || boxFormat != null;
        }
    }
}
