package com.ecom.orderapi.repository;

import com.ecom.orderapi.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity ,Long>
{

}
