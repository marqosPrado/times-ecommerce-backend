package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientAggregate, Integer>, JpaSpecificationExecutor<ClientAggregate> {
    Optional<ClientAggregate> findByCpf(String cpf);
    Boolean existsByCpf(String cpf);
    Boolean existsByEmail(String email);

    Optional<UserDetails> findByEmail(String email);

    Long countByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
}
