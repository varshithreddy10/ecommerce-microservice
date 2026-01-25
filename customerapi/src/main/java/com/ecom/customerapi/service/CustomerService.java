package com.ecom.customerapi.service;

import com.ecom.customerapi.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto addNewCustomerserv(CustomerDto customerdto);

    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerByIdserv(Long customerId);

    CustomerDto updateCustomerServ(Long customerId ,CustomerDto customerdto);

    String deleteCustomerServ(Long customerId);
}
