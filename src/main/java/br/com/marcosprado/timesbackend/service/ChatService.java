package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.Product;
import br.com.marcosprado.timesbackend.dto.chat.ChatMessageRequest;
import br.com.marcosprado.timesbackend.dto.chat.ChatMessageResponse;
import br.com.marcosprado.timesbackend.dto.product.response.ProductFilterResponse;
import br.com.marcosprado.timesbackend.enums.Gender;
import br.com.marcosprado.timesbackend.repository.ProductRepository;
import br.com.marcosprado.timesbackend.service.ia.IAChat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public ChatMessageResponse processMessage(ChatMessageRequest request) {
        String aiResponse = geminiChatService.sendMessage(
                request.message(),
                request.conversationHistory()
        );

        String conversationId = request.conversationId() != null
                ? request.conversationId()
                : UUID.randomUUID().toString();

        SearchCriteria searchCriteria = extractSearchCriteria(aiResponse);

        if (searchCriteria != null && searchCriteria.hasValidCriteria()) {
            String cleanedMessage = removeSearchCriteriaFromMessage(aiResponse);

            List<ProductFilterResponse> products = searchProducts(searchCriteria);

            return new ChatMessageResponse(cleanedMessage, conversationId, products);
        }

        return new ChatMessageResponse(aiResponse, conversationId);
    }

    private SearchCriteria extractSearchCriteria(String aiResponse) {
        try {
            Pattern pattern = Pattern.compile("CRITÉRIOS_BUSCA:\\s*\\{[^}]*\\}", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(aiResponse);

            if (matcher.find()) {
                String jsonStr = matcher.group();
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
            return null;
        }

        return null;
    }

    private String removeSearchCriteriaFromMessage(String message) {
        return message.replaceAll("CRITÉRIOS_BUSCA:\\s*\\{[^}]*\\}", "").trim();
    }

    private String getStringField(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull() || field.asText().isBlank()) {
            return null;
        }
        return field.asText();
    }

    private List<ProductFilterResponse> searchProducts(SearchCriteria criteria) {
        try {
            List<Product> products = productRepository.findByPartialCharacteristics(
                    criteria.brand,
                    criteria.style,
                    criteria.mechanism,
                    criteria.boxMaterial,
                    criteria.boxFormat,
                    criteria.dial,
                    criteria.line
            );

            if (criteria.gender != null) {
                try {
                    Gender gender = Gender.valueOf(criteria.gender.toUpperCase());
                    products = products.stream()
                            .filter(p -> p.getGender() == gender)
                            .toList();
                } catch (IllegalArgumentException e) {
                }
            }

            List<Product> limitedProducts = products.stream()
                    .limit(10)
                    .toList();

            return limitedProducts.stream()
                    .map(ProductFilterResponse::fromEntity)
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }

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
