package com.ecom.orderapi.repository;

import com.ecom.orderapi.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity ,Long> {
    List<OrderItemEntity> findAllByOrder_OrderId(Long orderId);
}
