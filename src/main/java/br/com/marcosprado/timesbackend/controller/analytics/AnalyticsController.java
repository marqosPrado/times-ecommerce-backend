package br.com.marcosprado.timesbackend.controller.analytics;

import br.com.marcosprado.timesbackend.dto.analytics.response.BasicSummaryAnalyticsResponse;
import br.com.marcosprado.timesbackend.dto.analytics.response.SalesVolumeResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<BasicSummaryAnalyticsResponse>> getBasicAnalytics() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(analyticsService.getBasicAnalytics()));
    }

    @GetMapping("/sales-volume")
    public ResponseEntity<ApiResponse<SalesVolumeResponse>> getSalesVolume(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String filterType
    ) {
        var response = analyticsService.getSalesVolumeByPeriod(startDate, endDate, filterType);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }
}
