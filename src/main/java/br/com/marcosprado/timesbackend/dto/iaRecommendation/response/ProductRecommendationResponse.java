package br.com.marcosprado.timesbackend.dto.iaRecommendation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

public record ProductRecommendationResponse(
        @JsonProperty("customer_id") Integer customerId,
        @JsonProperty("recommendations") List<RecommendedProduct> recommendations,
        @JsonProperty("total_recommendations") Integer totalRecommendations
) {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public record RecommendedProduct(
            @JsonProperty("gender") String gender,
            @JsonProperty("box_format") String boxFormat,
            @JsonProperty("box_material") String boxMaterial,
            @JsonProperty("brand") String brand,
            @JsonProperty("dial") String dial,
            @JsonProperty("line") String line,
            @JsonProperty("mechanism") String mechanism,
            @JsonProperty("style") String style,
            @JsonProperty("confidence_score") BigDecimal confidenceScore,
            @JsonProperty("reason") String reason
    ) {}

    public boolean hasRecommendations() {
        return recommendations != null && !recommendations.isEmpty();
    }

    public static ProductRecommendationResponse fromJson(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, ProductRecommendationResponse.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Erro ao desserializar JSON para ProductRecommendationResponse", e);
        }
    }
}