package com.ecom.orderapi.repository;


import com.ecom.orderapi.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    //List<ProductEntity> findByCategoryId();

    //List<ProductEntity> findByProductNameLikeIgnoreCase(String s);

    List<ProductEntity> findByNameLikeIgnoreCase(String s);

    List<ProductEntity> findByCategoryCategoryId(Long categoryId);
}
