package br.com.marcosprado.timesbackend.service;

import br.com.marcosprado.timesbackend.aggregate.ExchangeRequestVoucher;
import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeRequest;
import br.com.marcosprado.timesbackend.dto.exchangeVoucher.ExchangeVoucherSummaryResponse;
import br.com.marcosprado.timesbackend.exception.ResourceNotFoundException;
import br.com.marcosprado.timesbackend.repository.ExchangeVoucherRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeRequestVoucherService {

    private final ExchangeVoucherRequestRepository exchangeVoucherRequestRepository;
    private final ClientService clientService;

    public ExchangeRequestVoucherService(
            ExchangeVoucherRequestRepository exchangeVoucherRequestRepository,
            ClientService clientService
    ) {
        this.exchangeVoucherRequestRepository = exchangeVoucherRequestRepository;
        this.clientService = clientService;
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

    public Page<ExchangeVoucherSummaryResponse> getAllByPage(int page, int size, Integer currentUserId) {
        var user = clientService.findClientById(currentUserId)
                .orElseThrow(() -> ResourceNotFoundException.clientNotFound(currentUserId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<ExchangeRequestVoucher> vouchers = exchangeVoucherRequestRepository.findAllByClient(user, pageable);

        return vouchers.map(ExchangeVoucherSummaryResponse::fromEntity);
    }
}
