package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.StateAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<StateAggregate, Integer> {
}
