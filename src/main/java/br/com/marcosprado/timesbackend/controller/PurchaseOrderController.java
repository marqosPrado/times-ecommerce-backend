package br.com.marcosprado.timesbackend.controller;

import br.com.marcosprado.timesbackend.dto.CreatePurchaseOrderRequest;
import br.com.marcosprado.timesbackend.dto.PurchaseOrderResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.PurchaseOrderService;
import br.com.marcosprado.timesbackend.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> createOrder(
            @Valid @RequestBody CreatePurchaseOrderRequest request
    ) {
        Integer clientId = SecurityUtils.getCurrentUserId();

        PurchaseOrderResponse response = purchaseOrderService.create(request, clientId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PurchaseOrderResponse>>>  getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Integer clientId = SecurityUtils.getCurrentUserId();

        Page<PurchaseOrderResponse> response = purchaseOrderService.findAllByClient(clientId, page, size);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> markAsApprove(
            @PathVariable String id) {
        var response = ApiResponse.success(purchaseOrderService.aprovePurchaseOrder(Long.parseLong(id)));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/in-transit")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> markAsInTransit(
            @PathVariable String id
    ) {
        var response = ApiResponse.success(purchaseOrderService.markPurchaseOrderAsInTransit(Long.parseLong(id)));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/delivered")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> markAsDelivered(
            @PathVariable String id
    ) {
        var response = ApiResponse.success(purchaseOrderService.markPurchaseOrderAsDelivered(Long.parseLong(id)));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
