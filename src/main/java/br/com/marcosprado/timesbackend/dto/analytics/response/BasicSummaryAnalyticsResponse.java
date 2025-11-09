package br.com.marcosprado.timesbackend.dto.analytics.response;

public record BasicSummaryAnalyticsResponse(
        TotalCustomerAnalyticsResponse totalCustomer,
        long pendingOrders,
        long openExchanges,
        long deliveryToday
) {
}
