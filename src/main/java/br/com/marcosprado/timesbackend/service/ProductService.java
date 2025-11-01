package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.Product;
import br.com.marcosprado.timesbackend.dto.ProductSummaryResponse;
import br.com.marcosprado.timesbackend.dto.product.request.ProductFilterRequest;
import br.com.marcosprado.timesbackend.dto.product.response.ProductDetailResponse;
import br.com.marcosprado.timesbackend.dto.product.response.ProductFilterResponse;
import br.com.marcosprado.timesbackend.exception.ResourceNotFoundException;
import br.com.marcosprado.timesbackend.repository.ProductRepository;
import br.com.marcosprado.timesbackend.specification.product.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public ProductDetailResponse findByIdWithException(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(id));

        return ProductDetailResponse.fromEntity(product);
    }

    public Page<ProductFilterResponse> findAll(ProductFilterRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Product> specification = buildSpecification(request);

        Page<Product> productPage = productRepository.findAll(specification, pageable);

        return productPage.map(ProductFilterResponse::fromEntity);
    }

    private Specification<Product> buildSpecification(ProductFilterRequest request) {
        Specification<Product> spec = Specification.where(null);

        if (request.gender() != null) {
            spec = spec.and(ProductSpecification.hasGender(request.gender()));
        }

        if (request.brand() != null && !request.brand().isBlank()) {
            spec = spec.and(ProductSpecification.hasBrand(request.brand()));
        }

        if (request.boxMaterial() != null && !request.boxMaterial().isBlank()) {
            spec = spec.and(ProductSpecification.hasBoxMaterial(request.boxMaterial()));
        }

        if (request.mechanism() != null && !request.mechanism().isBlank()) {
            spec = spec.and(ProductSpecification.hasMechanism(request.mechanism()));
        }

        if (request.minPrice() != null && request.maxPrice() != null) {
            spec = spec.and(ProductSpecification.hasPriceRange(request.minPrice(), request.maxPrice()));
        }

        return spec;
    }

}
