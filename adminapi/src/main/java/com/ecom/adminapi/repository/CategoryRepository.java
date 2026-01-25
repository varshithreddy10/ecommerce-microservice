package com.ecom.adminapi.repository;


import com.ecom.adminapi.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ProductCategory, Long>
{
}
