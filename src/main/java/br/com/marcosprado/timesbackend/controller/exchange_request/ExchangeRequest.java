package br.com.marcosprado.timesbackend.controller.exchange_request;

import br.com.marcosprado.timesbackend.dto.exchange_voucher.request.CreateExchangeVoucherRequest;
import br.com.marcosprado.timesbackend.dto.exchange_voucher.response.ExchangeRequestResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.ExchangeVoucherRequestService;
import br.com.marcosprado.timesbackend.util.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exchange")
@CrossOrigin(origins = "*")
public class ExchangeRequest {

    private final ExchangeVoucherRequestService exchangeVoucherRequestService;

    public ExchangeRequest(ExchangeVoucherRequestService exchangeVoucherRequestService) {
        this.exchangeVoucherRequestService = exchangeVoucherRequestService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExchangeRequestResponse>> requestExchange(
            @RequestBody CreateExchangeVoucherRequest request
    ) {
        Integer clientId = SecurityUtils.getCurrentUserId();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(exchangeVoucherRequestService.requestExchange(request, clientId)));
    }
}
