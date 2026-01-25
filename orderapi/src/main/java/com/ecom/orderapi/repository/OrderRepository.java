package com.ecom.orderapi.repository;

import com.ecom.orderapi.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity , Long>
{

    //List<OrderEntity> findBycustomer_id();

    List<OrderEntity> findByCustomer_CustomerId(Long customerId);
}
