package br.com.marcosprado.timesbackend.dto;

import br.com.marcosprado.timesbackend.aggregate.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductSummaryResponse(
        Long id,

        @JsonProperty("title")
        String title,

        @JsonProperty("brand")
        String brand,

        @JsonProperty("price")
        String price,

        @JsonProperty("main_image")
        String mainImage
) {

    public static ProductSummaryResponse fromEntity(Product product) {
        String mainImage = product.getImages() != null && !product.getImages().isEmpty()
                ? product.getImages().getFirst().getUrl()
                : null;

        return new ProductSummaryResponse(
                product.getId(),
                product.getTitle(),
                product.getBrand(),
                product.getPrice().toPlainString(),
                mainImage
        );
    }
}
