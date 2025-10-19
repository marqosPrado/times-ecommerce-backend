package br.com.marcosprado.timesbackend.controller.product;

import br.com.marcosprado.timesbackend.dto.product.request.ProductFilterRequest;
import br.com.marcosprado.timesbackend.dto.product.response.ProductFilterResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductFilterResponse>>> getProducts(
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "box_material", required = false) String boxMaterial,
            @RequestParam(value = "mechanism", required = false) String mechanism,
            @RequestParam(value = "min_price", required = false) String minPrice,
            @RequestParam(value = "max_price", required = false) String maxPrice,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ProductFilterRequest request = ProductFilterRequest.fromParams(
                gender,
                brand,
                boxMaterial,
                mechanism,
                minPrice,
                maxPrice
        );

        Page<ProductFilterResponse> response = productService.findAll(request, page, size);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
