package com.product.api.repository;

import com.product.api.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ProductCategory , Long> {
}
