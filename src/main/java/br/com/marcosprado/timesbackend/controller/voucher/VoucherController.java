package br.com.marcosprado.timesbackend.controller.voucher;

import br.com.marcosprado.timesbackend.dto.CreateVoucherRequest;
import br.com.marcosprado.timesbackend.dto.VoucherResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.dto.voucher.VoucherSummaryResponse;
import br.com.marcosprado.timesbackend.service.VoucherService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VoucherResponse>> createVoucher(
            @Valid @RequestBody CreateVoucherRequest request
    ) {
        VoucherResponse voucher = voucherService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(voucher));
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<VoucherSummaryResponse>> getVoucher(
            @PathParam("voucherCode") String voucherCode
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(voucherService.getByIdentifier(voucherCode))
        );
    }
}
