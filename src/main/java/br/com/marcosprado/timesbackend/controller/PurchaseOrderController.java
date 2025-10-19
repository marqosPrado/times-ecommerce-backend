package br.com.marcosprado.timesbackend.controller;

import br.com.marcosprado.timesbackend.dto.CreatePurchaseOrderRequest;
import br.com.marcosprado.timesbackend.dto.PurchaseOrderResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> createOrder(
            @Valid @RequestBody CreatePurchaseOrderRequest request
    ) {
        PurchaseOrderResponse response = purchaseOrderService.create(request, 1);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }
}
