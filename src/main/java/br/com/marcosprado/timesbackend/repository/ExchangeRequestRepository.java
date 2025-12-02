package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeRequest;
import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    long countExchangeRequestByExchangeStatusEquals(ExchangeStatus exchangeStatus);
    Page<ExchangeRequest> findByExchangeStatus(ExchangeStatus exchangeStatus, Pageable pageable);
}
