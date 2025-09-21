package br.com.marcosprado.timesbackend.repository.test;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TestDatabaseRepository extends JpaRepository<ClientAggregate, Long> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE clients CASCADE", nativeQuery = true)
    void truncateClientsTable();
}
