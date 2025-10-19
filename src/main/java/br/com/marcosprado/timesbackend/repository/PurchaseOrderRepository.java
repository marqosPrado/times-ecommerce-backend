package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
