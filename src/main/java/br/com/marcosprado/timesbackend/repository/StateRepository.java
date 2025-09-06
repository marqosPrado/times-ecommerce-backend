package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.StateAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<StateAggregate, Integer> {
    Optional<StateAggregate> findOneByState(String state);
}
