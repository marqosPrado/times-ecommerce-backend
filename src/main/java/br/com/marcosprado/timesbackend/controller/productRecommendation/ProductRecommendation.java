package br.com.marcosprado.timesbackend.controller.productRecommendation;

import br.com.marcosprado.timesbackend.dto.product.response.ProductFilterResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.ProductRecommendationService;
import br.com.marcosprado.timesbackend.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product-recommendation")
public class ProductRecommendation {
    private final ProductRecommendationService productRecommendationService;

    public ProductRecommendation(ProductRecommendationService productRecommendationService) {
        this.productRecommendationService = productRecommendationService;
    }

    @RequestMapping("/recommend")
    public ResponseEntity<ApiResponse<List<ProductFilterResponse>>> recommend() {
        Integer customerId = SecurityUtils.getCurrentUserId();

        var recommendedProduct = productRecommendationService.recommend(customerId);
        return ResponseEntity.ok(ApiResponse.success(recommendedProduct));
    }
}
