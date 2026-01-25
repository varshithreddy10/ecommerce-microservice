package com.ecom.cartapi.repository;

import com.ecom.cartapi.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>
{
}
