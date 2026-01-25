package com.ecom.cartapi.feignclient;

import com.ecom.cartapi.dto.AddressDto;
import com.ecom.cartapi.dto.CustomerDto;
import com.ecom.cartapi.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "API")
public interface Productapi_feignclient
{
    @GetMapping("/api/products/get/product/{productId}")
    public ProductDto getProductDetails(@PathVariable("productId") Long productId);


}
