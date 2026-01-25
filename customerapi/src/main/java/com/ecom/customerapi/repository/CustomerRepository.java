package com.ecom.customerapi.repository;

import com.ecom.customerapi.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity , Long>
{
}
