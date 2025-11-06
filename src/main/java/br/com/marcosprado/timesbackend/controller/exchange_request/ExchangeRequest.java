package br.com.marcosprado.timesbackend.controller.exchange_request;

import br.com.marcosprado.timesbackend.dto.exchange_voucher.request.CreateExchangeVoucherRequest;
import br.com.marcosprado.timesbackend.dto.exchange_voucher.response.ExchangeRequestResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.ExchangeRequestService;
import br.com.marcosprado.timesbackend.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exchange")
@CrossOrigin(origins = "*")
public class ExchangeRequest {

    private final ExchangeRequestService exchangeRequestService;

    public ExchangeRequest(ExchangeRequestService exchangeRequestService) {
        this.exchangeRequestService = exchangeRequestService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> requestExchange(
            @RequestBody CreateExchangeVoucherRequest request
    ) {
        Integer clientId = SecurityUtils.getCurrentUserId();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(exchangeRequestService.requestExchange(request, clientId)));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ExchangeRequestResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(exchangeRequestService.getAll(page, size)));
    }

    @PatchMapping("/{exchangeId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> approveExchange(@PathVariable String exchangeId) {
        var response = ApiResponse.success(exchangeRequestService.approveExchange(Long.parseLong(exchangeId)));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{exchangeId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> rejectExchange(@PathVariable String exchangeId) {
        var response = ApiResponse.success(exchangeRequestService.rejectExchange(Long.parseLong(exchangeId)));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{exchangeId}/in-transit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> markAsInTransit(@PathVariable String exchangeId) {
        var response = ApiResponse.success(exchangeRequestService.markAsInTransit(Long.parseLong(exchangeId)));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{exchangeId}/items-receive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> confirmItemsReceived(@PathVariable String exchangeId) {
        var response = ApiResponse.success(exchangeRequestService.confirmItemsReceived(Long.parseLong(exchangeId)));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{exchangeId}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> complete(@PathVariable String exchangeId) {
        var response = ApiResponse.success(exchangeRequestService.complete(Long.parseLong(exchangeId)));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
