package br.com.marcosprado.timesbackend.dto.iaRecommendation.request;

import br.com.marcosprado.timesbackend.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProductRecommendationRequest(
        @JsonProperty("customer") CustomerData customer,
        @JsonProperty("purchase_history") List<PurchaseHistoryData> purchaseHistory
) {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public record CustomerData(
            Integer id,
            String fullName,
            Gender gender,
            LocalDate birthDate,
            Integer age,
            LocalDate memberSince,
            BigDecimal totalSpent,
            Integer totalOrders
    ) {
        public CustomerData {
            age = birthDate != null ?
                    LocalDate.now().getYear() - birthDate.getYear() : null;
        }
    }

    public record PurchaseHistoryData(
            String orderNumber,
            LocalDateTime purchaseDate,
            BigDecimal totalAmount,
            List<PurchasedItemData> items
    ) {}

    public record PurchasedItemData(
            Long productId,
            String brand,
            String title,
            BigDecimal unitPrice,
            Integer quantity,
            Gender gender,
            String line,
            String style,
            String mechanism,
            String boxMaterial,
            String boxFormat,
            String dial
    ) {}

    public boolean isValid() {
        return customer != null &&
                purchaseHistory != null &&
                !purchaseHistory.isEmpty();
    }

    public String toJson() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Erro ao serializar ProductRecommendationRequest para JSON", e);
        }
    }
}
