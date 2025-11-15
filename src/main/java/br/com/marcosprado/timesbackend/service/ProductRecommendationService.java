package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.Product;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderStatus;
import br.com.marcosprado.timesbackend.dto.iaRecommendation.request.ProductRecommendationRequestBuilder;
import br.com.marcosprado.timesbackend.dto.product.response.ProductFilterResponse;
import br.com.marcosprado.timesbackend.exception.ResourceNotFoundException;
import br.com.marcosprado.timesbackend.repository.ProductRepository;
import br.com.marcosprado.timesbackend.repository.PurchaseOrderRepository;
import br.com.marcosprado.timesbackend.service.ia.IARecommendation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductRecommendationService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final ClientService clientService;
    private final IARecommendation iaRecommendationService;

    public ProductRecommendationService(
            PurchaseOrderRepository purchaseOrderRepository,
            ProductRepository productRepository,
            ClientService clientService,
            IARecommendation iaRecommendationService
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productRepository = productRepository;
        this.clientService = clientService;
        this.iaRecommendationService = iaRecommendationService;
    }

    public List<ProductFilterResponse> recommend(Integer customerId) {
        if (customerId == null) throw new IllegalArgumentException("CustomerId não pode ser nulo");

        var customer = clientService.findClientById(customerId)
                .orElseThrow(() -> ResourceNotFoundException.clientNotFound(customerId));

        var purchaseOrderHistory = purchaseOrderRepository.findAllByClientAndOrderStatusEquals(
                customer,
                OrderStatus.DELIVERED
        ).stream().toList();

        var recommendationRequest = ProductRecommendationRequestBuilder
                .builder(customer, purchaseOrderHistory)
                .build();

        if (!recommendationRequest.isValid()) {
            return List.of();
        }

        var productRecommendation = iaRecommendationService.getProductRecommendation(recommendationRequest);

        if (productRecommendation == null || !productRecommendation.hasRecommendations()) {
            return List.of();
        }

        List<Product> products = productRecommendation.recommendations().stream()
                .flatMap(recommendedProduct -> productRepository.findByPartialCharacteristics(
                        recommendedProduct.brand(),
                        recommendedProduct.style(),
                        recommendedProduct.mechanism(),
                        recommendedProduct.boxMaterial(),
                        recommendedProduct.boxFormat(),
                        recommendedProduct.dial(),
                        recommendedProduct.line()
                ).stream())
                .distinct()
                .toList();

        return ProductFilterResponse.fromEntity(products);
    }
}