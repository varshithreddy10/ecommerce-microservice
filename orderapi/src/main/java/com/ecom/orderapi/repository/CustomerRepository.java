package com.ecom.orderapi.repository;

import com.ecom.orderapi.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity , Long>
{
}
