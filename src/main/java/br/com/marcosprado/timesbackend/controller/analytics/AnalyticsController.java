package br.com.marcosprado.timesbackend.controller.analytics;

import br.com.marcosprado.timesbackend.dto.analytics.response.BasicSummaryAnalyticsResponse;
import br.com.marcosprado.timesbackend.dto.response.ApiResponse;
import br.com.marcosprado.timesbackend.service.AnalyticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
