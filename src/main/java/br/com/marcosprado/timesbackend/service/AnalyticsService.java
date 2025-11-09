package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeStatus;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderStatus;
import br.com.marcosprado.timesbackend.dto.analytics.response.BasicSummaryAnalyticsResponse;
import br.com.marcosprado.timesbackend.dto.analytics.response.TotalCustomerAnalyticsResponse;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.repository.ExchangeRequestRepository;
import br.com.marcosprado.timesbackend.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AnalyticsService {
    private final ClientRepository clientRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;

    public AnalyticsService(
            ClientRepository clientRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            ExchangeRequestRepository exchangeRequestRepository
    ) {
        this.clientRepository = clientRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.exchangeRequestRepository = exchangeRequestRepository;
    }

    public BasicSummaryAnalyticsResponse getBasicAnalytics() {
        var totalCustomerAnalytics = getTotalCustomerAnalytics();
        var totalPendingOrders = purchaseOrderRepository.countPurchaseOrderByOrderStatusEquals(OrderStatus.PROCESSING);
        var totalExchangePending = exchangeRequestRepository.countExchangeRequestByExchangeStatusEquals(ExchangeStatus.PENDING);
        var totalPurchaseOrderDeliveredToday = purchaseOrderRepository.countPurchaseOrderByInTransitAt(LocalDate.now());

        return new BasicSummaryAnalyticsResponse(
                totalCustomerAnalytics,
                totalPendingOrders,
                totalExchangePending,
                totalPurchaseOrderDeliveredToday
        );
    }

    private TotalCustomerAnalyticsResponse getTotalCustomerAnalytics() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime lastMonth = today.minusMonths(1);
        LocalDateTime twoMonthsAgo = today.minusMonths(2);

        var totalCustomers = clientRepository.count();

        var currentMonthCustomers = clientRepository.countByCreatedAtBetween(
                lastMonth.toLocalDate(),
                today.toLocalDate()
        );

        var previousMonthCustomers = clientRepository.countByCreatedAtBetween(
                twoMonthsAgo.toLocalDate(),
                lastMonth.toLocalDate()
        );

        double percentageDifference = 0.0;
        if (previousMonthCustomers > 0) {
            percentageDifference = ((double) (currentMonthCustomers - previousMonthCustomers)
                    / previousMonthCustomers) * 100;
        }

        return new TotalCustomerAnalyticsResponse(
                totalCustomers,
                percentageDifference
        );
    }
}
