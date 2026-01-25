package com.product.api.service;

import com.product.api.dto.CategoryDto;
import com.product.api.dto.ProductDto;
import com.product.api.dto.UpdateProductDto;

import java.util.List;

public interface ProductService
{
    public List<ProductDto> getAllProducts();
    public List<ProductDto> getAllProductsbyCategory(Long CategoryId);
    public ProductDto createProduct(ProductDto productdto , Long CategoryId);
    public  void deleteProduct(Long productId);

    List<ProductDto> findProductByName(String s);

    CategoryDto createCategory(CategoryDto categorydto);

    ProductDto getProductByproductId(Long productId);

    String updateProduct(List<UpdateProductDto> updateproductdto);
}
