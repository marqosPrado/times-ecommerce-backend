package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeStatus;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderStatus;
import br.com.marcosprado.timesbackend.dto.analytics.response.BasicSummaryAnalyticsResponse;
import br.com.marcosprado.timesbackend.dto.analytics.response.SalesVolumeResponse;
import br.com.marcosprado.timesbackend.dto.analytics.response.SeriesData;
import br.com.marcosprado.timesbackend.dto.analytics.response.TotalCustomerAnalyticsResponse;
import br.com.marcosprado.timesbackend.repository.ClientRepository;
import br.com.marcosprado.timesbackend.repository.ExchangeRequestRepository;
import br.com.marcosprado.timesbackend.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public SalesVolumeResponse getSalesVolumeByPeriod(
            LocalDate startDate,
            LocalDate endDate,
            String filterType
    ) {
        List<String> dates = generateDateLabels(startDate, endDate);

        List<SeriesData> series = switch (filterType) {
            case "BRAND" -> getSalesVolumeByBrand(startDate, endDate, dates);
            case "LINE" -> getSalesVolumeByLine(startDate, endDate, dates);
            case "STYLE" -> getSalesVolumeByStyle(startDate, endDate, dates);
            case "MECHANISM" -> getSalesVolumeByMechanism(startDate, endDate, dates);
            case "GENDER" -> getSalesVolumeByGender(startDate, endDate, dates);
            default -> getSalesVolumeByProduct(startDate, endDate, dates);
        };

        return new SalesVolumeResponse(dates, series);
    }

    private List<SeriesData> getSalesVolumeByProduct(
            LocalDate startDate,
            LocalDate endDate,
            List<String> dates
    ) {
        List<Object[]> rawData = purchaseOrderRepository
                .findSalesVolumeByProductAndDate(startDate, endDate);

        return processRawData(rawData, startDate, endDate);
    }

    private List<SeriesData> getSalesVolumeByBrand(
            LocalDate startDate,
            LocalDate endDate,
            List<String> dates
    ) {
        List<Object[]> rawData = purchaseOrderRepository
                .findSalesVolumeByBrandAndDate(startDate, endDate);

        return processRawData(rawData, startDate, endDate);
    }

    private List<SeriesData> getSalesVolumeByLine(
            LocalDate startDate,
            LocalDate endDate,
            List<String> dates
    ) {
        List<Object[]> rawData = purchaseOrderRepository
                .findSalesVolumeByLineAndDate(startDate, endDate);

        return processRawData(rawData, startDate, endDate);
    }

    private List<SeriesData> getSalesVolumeByStyle(
            LocalDate startDate,
            LocalDate endDate,
            List<String> dates
    ) {
        List<Object[]> rawData = purchaseOrderRepository
                .findSalesVolumeByStyleAndDate(startDate, endDate);

        return processRawData(rawData, startDate, endDate);
    }

    private List<SeriesData> getSalesVolumeByMechanism(
            LocalDate startDate,
            LocalDate endDate,
            List<String> dates
    ) {
        List<Object[]> rawData = purchaseOrderRepository
                .findSalesVolumeByMechanismAndDate(startDate, endDate);

        return processRawData(rawData, startDate, endDate);
    }

    private List<SeriesData> getSalesVolumeByGender(
            LocalDate startDate,
            LocalDate endDate,
            List<String> dates
    ) {
        List<Object[]> rawData = purchaseOrderRepository
                .findSalesVolumeByGenderAndDate(startDate, endDate);

        return processRawData(rawData, startDate, endDate);
    }

    private List<SeriesData> processRawData(
            List<Object[]> rawData,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<String, Map<LocalDate, Long>> salesMap = new HashMap<>();

        for (Object[] row : rawData) {
            String name = (String) row[0];
            LocalDate saleDate = ((java.sql.Date) row[1]).toLocalDate();
            Long quantity = ((Number) row[2]).longValue();

            salesMap
                    .computeIfAbsent(name, k -> new HashMap<>())
                    .put(saleDate, quantity);
        }

        List<SeriesData> series = new ArrayList<>();
        for (Map.Entry<String, Map<LocalDate, Long>> entry : salesMap.entrySet()) {
            String name = entry.getKey();
            Map<LocalDate, Long> salesByDate = entry.getValue();

            List<Long> values = new ArrayList<>();
            LocalDate currentDate = startDate;

            while (!currentDate.isAfter(endDate)) {
                values.add(salesByDate.getOrDefault(currentDate, 0L));
                currentDate = currentDate.plusDays(1);
            }

            series.add(new SeriesData(name, values));
        }

        return series.stream()
                .sorted((s1, s2) -> {
                    long sum1 = s1.values().stream().mapToLong(Long::longValue).sum();
                    long sum2 = s2.values().stream().mapToLong(Long::longValue).sum();
                    return Long.compare(sum2, sum1);
                })
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<String> generateDateLabels(LocalDate startDate, LocalDate endDate) {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate.format(formatter));
            currentDate = currentDate.plusDays(1);
        }

        return dates;
    }
}
