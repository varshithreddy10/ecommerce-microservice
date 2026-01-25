package com.ecom.orderapi.feignclient;

import com.ecom.orderapi.dto.ProductDto;
import com.ecom.orderapi.dto.UpdateProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "API")
public interface ProductApi_feignclient
{
    @GetMapping("/api/products/get/product/{productId}")
    public ProductDto getProductByProductid(@PathVariable("productId") Long productId);

    @PutMapping("/api/products/update/purchasedproduct")
    public String updateAllProducts(@RequestBody List<UpdateProductDto> allproductsupdate);


}
