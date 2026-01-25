package com.ecom.orderapi.feignclient;

import com.ecom.orderapi.dto.AddressDto;
import com.ecom.orderapi.dto.CustomerDto;
import com.ecom.orderapi.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMERAPI")
public interface CustomerApi_feignclient
{
    @GetMapping("/api/customer/get/customer/{customerId}")
    CustomerDto getCustomerFeign(@PathVariable("customerId") Long customerId);

    @GetMapping("/api/customer/get/logged/customerdetails")
    CustomerDto getCustomerDetailsByHeader();

    @GetMapping("/api/address/delete/address/{addressId}")
    AddressDto getAddressFeign(@PathVariable("addressId") Long addressId);


}
