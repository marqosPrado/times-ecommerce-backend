package br.com.marcosprado.timesbackend.service.ia;

import br.com.marcosprado.timesbackend.dto.iaRecommendation.request.ProductRecommendationRequest;
import br.com.marcosprado.timesbackend.dto.iaRecommendation.response.ProductRecommendationResponse;

public interface IARecommendation {
    ProductRecommendationResponse getProductRecommendation(ProductRecommendationRequest request);
}
