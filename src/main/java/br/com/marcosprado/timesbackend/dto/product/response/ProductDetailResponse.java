package br.com.marcosprado.timesbackend.dto.product.response;

import br.com.marcosprado.timesbackend.aggregate.ImageAggregate;
import br.com.marcosprado.timesbackend.aggregate.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProductDetailResponse(
        Long id,
        String brand,
        String title,
        String price,
        String gender,
        String line,
        String style,
        String mechanism,

        @JsonProperty("box_material")
        String boxMaterial,

        @JsonProperty("box_format")
        String boxFormat,

        String dial,
        List<String> images
) {
    public static ProductDetailResponse fromEntity(Product product) {
        List<String> imageUrls = product.getImages() != null
                ? product.getImages().stream()
                .map(ImageAggregate::getUrl)
                .toList()
                : List.of();

        return new ProductDetailResponse(
                product.getId(),
                product.getBrand(),
                product.getTitle(),
                product.getPrice().toPlainString(),
                product.getGender().name(),
                product.getLine(),
                product.getStyle(),
                product.getMechanism(),
                product.getBoxMaterial(),
                product.getBoxFormat(),
                product.getDial(),
                imageUrls
        );
    }
}