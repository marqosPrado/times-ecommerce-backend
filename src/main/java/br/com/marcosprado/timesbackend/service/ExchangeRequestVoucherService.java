package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.ExchangeRequestVoucher;
import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeRequest;
import br.com.marcosprado.timesbackend.repository.ExchangeVoucherRequestRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeRequestVoucherService {

    private final ExchangeVoucherRequestRepository exchangeVoucherRequestRepository;

    public ExchangeRequestVoucherService(ExchangeVoucherRequestRepository exchangeVoucherRequestRepository) {
        this.exchangeVoucherRequestRepository = exchangeVoucherRequestRepository;
    }

    public ExchangeRequestVoucher generateByExchangeRequest(ExchangeRequest exchangeRequest) {
        if (exchangeRequest == null) {
            throw new IllegalArgumentException("Não existe pedido de troca para gerar cupom de troca");
        }

        return new ExchangeRequestVoucher(
                exchangeRequest.getExchangeValue(),
                exchangeRequest.getClient()
        );
    }

    public void generate(BigDecimal amount, ClientAggregate client) {
        if (amount == null || client == null) {
            throw new IllegalArgumentException(
                    String.format("Não foi possível gerar cupom de troca, amount: %s, client: %s", amount, client)
            );
        }
        exchangeVoucherRequestRepository.save(new ExchangeRequestVoucher(amount, client));
    }

    public void disable(ExchangeRequestVoucher voucher) {
        voucher.disable();
        exchangeVoucherRequestRepository.save(voucher);
    }
}
