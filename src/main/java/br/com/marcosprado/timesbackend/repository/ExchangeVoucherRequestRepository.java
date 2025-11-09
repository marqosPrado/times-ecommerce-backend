package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.ExchangeRequestVoucher;
import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeVoucherRequestRepository extends JpaRepository<ExchangeRequestVoucher, Long> {
    Page<ExchangeRequestVoucher> findAllByClient(ClientAggregate client, Pageable pageable);
}
