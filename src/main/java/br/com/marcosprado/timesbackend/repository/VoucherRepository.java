package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findVoucherByIdentifier(String identifier);
}
