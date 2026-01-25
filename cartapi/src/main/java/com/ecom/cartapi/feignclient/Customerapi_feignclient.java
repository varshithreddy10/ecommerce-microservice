package com.ecom.cartapi.feignclient;

import com.ecom.cartapi.dto.AddressDto;
import com.ecom.cartapi.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "CUSTOMERAPI")
public interface Customerapi_feignclient
{
    @GetMapping("/api/customer/get/customer/{customerId}")
    CustomerDto getCustomerDetails(@PathVariable("customerId") Long customerId);

    @GetMapping("/api/address/get/address/{addressId}")
    AddressDto getAddressDetails(@PathVariable("addressId") Long addressId);
}
