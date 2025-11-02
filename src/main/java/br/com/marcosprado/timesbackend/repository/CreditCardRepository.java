package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.CreditCardAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardAggregate, Integer> {
    List<CreditCardAggregate> findAllById(Integer id);

    List<CreditCardAggregate> findAllByClient(ClientAggregate client);
}
