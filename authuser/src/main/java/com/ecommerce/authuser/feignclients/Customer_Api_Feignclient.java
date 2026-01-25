package com.ecommerce.authuser.feignclients;


import com.ecommerce.authuser.dto.CustomerCreateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CUSTOMERAPI")
public interface Customer_Api_Feignclient
{
    @PostMapping("/api/customer/add/customer")
    CustomerCreateDto createCustomer(@RequestBody CustomerCreateDto customercreatedto);
}
