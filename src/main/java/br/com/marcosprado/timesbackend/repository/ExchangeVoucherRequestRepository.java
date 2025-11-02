package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.exchange_request.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeVoucherRequestRepository extends JpaRepository<ExchangeRequest, Long> {
}
