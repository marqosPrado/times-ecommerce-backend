package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.AddressAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressAggregate, Integer> {
}
