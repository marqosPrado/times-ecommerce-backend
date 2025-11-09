package br.com.marcosprado.timesbackend.dto.analytics.response;

public record TotalCustomerAnalyticsResponse(
        long totalCustomer,
        double growthTotalCustomer
) {
}
