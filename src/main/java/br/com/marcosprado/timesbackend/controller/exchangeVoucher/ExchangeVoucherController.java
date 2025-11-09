package br.com.marcosprado.timesbackend.controller.exchangeVoucher;

import br.com.marcosprado.timesbackend.dto.exchangeVoucher.ExchangeVoucherSummaryResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.ExchangeRequestVoucherService;
import br.com.marcosprado.timesbackend.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange-voucher")
public class ExchangeVoucherController {
    public final ExchangeRequestVoucherService service;

    public ExchangeVoucherController(ExchangeRequestVoucherService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ExchangeVoucherSummaryResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(service.getAllByPage(page, size, currentUserId)));
    }
}
