package br.com.marcosprado.timesbackend.repository;

import br.com.marcosprado.timesbackend.aggregate.client.ClientAggregate;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.OrderStatus;
import br.com.marcosprado.timesbackend.aggregate.purchase_order.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Page<PurchaseOrder> findAllByClient(ClientAggregate client, Pageable pageable);

    Collection<PurchaseOrder> findAllByClientAndOrderStatusEquals(ClientAggregate client, OrderStatus orderStatus);

    long countPurchaseOrderByOrderStatusEquals(OrderStatus orderStatus);

    long countPurchaseOrderByDeliveredAt(LocalDateTime deliveredAt);

    long countPurchaseOrderByInTransitAt(LocalDate now);

    @Query("""
        SELECT p.title, CAST(po.createdAt AS date), SUM(oi.quantity)
        FROM PurchaseOrder po
        JOIN po.items oi
        JOIN oi.product p
        WHERE CAST(po.createdAt AS date) BETWEEN :startDate AND :endDate
        AND po.orderStatus = 'APPROVED'
        GROUP BY p.title, CAST(po.createdAt AS date)
        ORDER BY CAST(po.createdAt AS date)
    """)
    List<Object[]> findSalesVolumeByProductAndDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Query("""
        SELECT p.brand, CAST(po.createdAt AS date), SUM(oi.quantity)
        FROM PurchaseOrder po
        JOIN po.items oi
        JOIN oi.product p
        WHERE CAST(po.createdAt AS date) BETWEEN :startDate AND :endDate
        AND po.orderStatus = 'APPROVED'
        GROUP BY p.brand, CAST(po.createdAt AS date)
        ORDER BY CAST(po.createdAt AS date)
    """)
    List<Object[]> findSalesVolumeByBrandAndDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT p.line, CAST(po.createdAt AS date), SUM(oi.quantity)
        FROM PurchaseOrder po
        JOIN po.items oi
        JOIN oi.product p
        WHERE CAST(po.createdAt AS date) BETWEEN :startDate AND :endDate
        AND po.orderStatus = 'APPROVED'
        GROUP BY p.line, CAST(po.createdAt AS date)
        ORDER BY CAST(po.createdAt AS date)
    """)
    List<Object[]> findSalesVolumeByLineAndDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT p.style, CAST(po.createdAt AS date), SUM(oi.quantity)
        FROM PurchaseOrder po
        JOIN po.items oi
        JOIN oi.product p
        WHERE CAST(po.createdAt AS date) BETWEEN :startDate AND :endDate
        AND po.orderStatus = 'APPROVED'
        GROUP BY p.style, CAST(po.createdAt AS date)
        ORDER BY CAST(po.createdAt AS date)
    """)
    List<Object[]> findSalesVolumeByStyleAndDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT p.mechanism, CAST(po.createdAt AS date), SUM(oi.quantity)
        FROM PurchaseOrder po
        JOIN po.items oi
        JOIN oi.product p
        WHERE CAST(po.createdAt AS date) BETWEEN :startDate AND :endDate
        AND po.orderStatus = 'APPROVED'
        GROUP BY p.mechanism, CAST(po.createdAt AS date)
        ORDER BY CAST(po.createdAt AS date)
    """)
    List<Object[]> findSalesVolumeByMechanismAndDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT CAST(p.gender AS string), CAST(po.createdAt AS date), SUM(oi.quantity)
        FROM PurchaseOrder po
        JOIN po.items oi
        JOIN oi.product p
        WHERE CAST(po.createdAt AS date) BETWEEN :startDate AND :endDate
        AND po.orderStatus = 'APPROVED'
        GROUP BY p.gender, CAST(po.createdAt AS date)
        ORDER BY CAST(po.createdAt AS date)
    """)
    List<Object[]> findSalesVolumeByGenderAndDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
