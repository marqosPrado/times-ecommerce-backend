package br.com.marcosprado.timesbackend.dto.product.response;

import br.com.marcosprado.timesbackend.aggregate.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductFilterResponse(
        Long id,

        @JsonProperty("title")
        String title,

        @JsonProperty("price")
        String price,

        @JsonProperty("url_image")
        String urlImage
) {

    public static ProductFilterResponse fromEntity(Product product) {
        return new ProductFilterResponse(
                product.getId(),
                product.getTitle(),
                product.getPrice().toPlainString(),
                product.getImages().getFirst().getUrl()
        );
    }
}
