package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.ExchangeRequestVoucher;
import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeRequest;
import br.com.marcosprado.timesbackend.repository.ExchangeVoucherRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRequestVoucherService {

    private final ExchangeVoucherRequestRepository exchangeVoucherRequestRepository;

    public ExchangeRequestVoucherService(ExchangeVoucherRequestRepository exchangeVoucherRequestRepository) {
        this.exchangeVoucherRequestRepository = exchangeVoucherRequestRepository;
    }

    public ExchangeRequestVoucher generate(ExchangeRequest exchangeRequest) {
        if (exchangeRequest == null) {
            throw new IllegalArgumentException("Não existe pedido de troca para gerar cupom de troca");
        }

        return new ExchangeRequestVoucher(
                exchangeRequest.getExchangeValue(),
                exchangeRequest.getClient()
        );
    }
}
