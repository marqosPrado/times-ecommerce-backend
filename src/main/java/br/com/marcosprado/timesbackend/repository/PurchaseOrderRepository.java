package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderStatus;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Page<PurchaseOrder> findAllByClient(ClientAggregate client, Pageable pageable);

    long countPurchaseOrderByOrderStatusEquals(OrderStatus orderStatus);

    long countPurchaseOrderByDeliveredAt(LocalDateTime deliveredAt);

    long countPurchaseOrderByInTransitAt(LocalDate now);
}
