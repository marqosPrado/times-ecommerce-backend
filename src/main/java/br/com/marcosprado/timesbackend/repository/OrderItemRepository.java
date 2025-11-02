package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
