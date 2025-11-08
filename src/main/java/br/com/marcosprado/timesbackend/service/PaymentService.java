package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.ExchangeRequestVoucher;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import br.com.marcosprado.timesbackend.exception.OperationNotAllowedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class PaymentService {

    private final ExchangeRequestVoucherService exchangeRequestVoucherService;

    public PaymentService(
            ExchangeRequestVoucherService exchangeRequestVoucherService
    ) {
        this.exchangeRequestVoucherService = exchangeRequestVoucherService;
    }

    public void process(PurchaseOrder purchaseOrder) {
        if (purchaseOrder == null) {
            throw new IllegalArgumentException("Purchase Order is null");
        }

        Set<ExchangeRequestVoucher> vouchers = purchaseOrder.getExchangeVouchersRequest();
        if (!vouchers.isEmpty()) {
            BigDecimal totalVouchers = vouchers.stream()
                    .map(ExchangeRequestVoucher::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal difference = totalVouchers.subtract(purchaseOrder.getTotal());

            if (difference.compareTo(BigDecimal.ZERO) < 0 && purchaseOrder.getCreditCard().isEmpty()) {
                throw OperationNotAllowedException.insufficientBalance();
            }

            if (difference.compareTo(BigDecimal.ZERO) > 0) {
                exchangeRequestVoucherService.generate(difference, purchaseOrder.getClient());
            }

            disableVouchers(vouchers);
        }
    }

    private void disableVouchers(Set<ExchangeRequestVoucher> vouchers) {
        vouchers.forEach(exchangeRequestVoucherService::disable);
    }
}
