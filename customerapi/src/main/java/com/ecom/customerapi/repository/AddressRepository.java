package com.ecom.customerapi.repository;

import com.ecom.customerapi.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity , Long>
{
    List<AddressEntity> findByCustomer_CustomerId(Long customerId);
}
