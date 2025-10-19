package br.com.marcosprado.timesbackend.dto.product.request;

import br.com.marcosprado.timesbackend.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ProductFilterRequest(
        Gender gender,
        String brand,

        @JsonProperty("box_material")
        String boxMaterial,
        String mechanism,

        @JsonProperty("min_price")
        BigDecimal minPrice,

        @JsonProperty("max_price")
        BigDecimal maxPrice
) {
    public static ProductFilterRequest fromParams(
            String genderStr,
            String brand,
            String boxMaterial,
            String mechanism,
            String minPriceStr,
            String maxPriceStr
    ) {
        Gender gender = null;
        if (genderStr != null && !genderStr.isBlank()) {
            try {
                gender = Gender.valueOf(genderStr.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }

        BigDecimal minPrice = null;
        if (minPriceStr != null && !minPriceStr.isBlank()) {
            try {
                minPrice = new BigDecimal(minPriceStr);
            } catch (NumberFormatException ignored) {
            }
        }

        BigDecimal maxPrice = null;
        if (maxPriceStr != null && !maxPriceStr.isBlank()) {
            try {
                maxPrice = new BigDecimal(maxPriceStr);
            } catch (NumberFormatException ignored) {
            }
        }

        return new ProductFilterRequest(
                gender,
                brand,
                boxMaterial,
                mechanism,
                minPrice,
                maxPrice
        );
    }
}