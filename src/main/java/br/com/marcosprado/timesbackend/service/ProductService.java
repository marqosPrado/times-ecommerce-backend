package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.Product;
import br.com.marcosprado.timesbackend.repository.ProductRepository;
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
}
