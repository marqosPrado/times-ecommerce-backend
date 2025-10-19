package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Page<PurchaseOrder> findAllByClientOrderByCreatedAtDesc(ClientAggregate client, Pageable pageable);
}
