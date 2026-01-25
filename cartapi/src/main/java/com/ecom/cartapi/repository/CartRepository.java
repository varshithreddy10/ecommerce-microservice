package com.ecom.cartapi.repository;

import com.ecom.cartapi.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity , Long>
{
    //CartEntity findByUserUserId(Long customerId);

    CartEntity findByUserCustomerId(Long customerId);
}
