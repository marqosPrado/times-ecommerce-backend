package br.com.marcosprado.timesbackend.dto.exchange_voucher.response;

import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeRequest;
import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeStatus;
import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record ExchangeRequestResponse(
        Long id,
        String exchangeNumber,
        String purchaseOrderNumber,
        Integer clientId,
        String clientName,
        ExchangeType exchangeType,
        ExchangeStatus exchangeStatus,
        List<ExchangeItemResponse> items,
        BigDecimal exchangeValue
) {

    public static ExchangeRequestResponse fromEntity(ExchangeRequest exchangeRequest) {
        return new ExchangeRequestResponse(
                exchangeRequest.getId(),
                exchangeRequest.getExchangeNumber(),
                exchangeRequest.getPurchaseOrder().getPurchaseOrderNumber(),
                exchangeRequest.getClient().getId(),
                exchangeRequest.getClient().getFullName(),
                exchangeRequest.getExchangeType(),
                exchangeRequest.getExchangeStatus(),
                exchangeRequest.getItensToExchange().stream()
                        .map(item -> new ExchangeItemResponse(
                                item.getId(),
                                item.getProduct().getTitle(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getSubTotal()
                        ))
                        .collect(Collectors.toList()),
                exchangeRequest.getExchangeValue()
        );
    }
}
