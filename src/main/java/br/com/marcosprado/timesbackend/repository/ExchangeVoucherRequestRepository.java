package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.ExchangeRequestVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeVoucherRequestRepository extends JpaRepository<ExchangeRequestVoucher, Long> {
}
