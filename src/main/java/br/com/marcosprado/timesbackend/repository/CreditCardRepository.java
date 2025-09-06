package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardAggregate, Integer> {
}
