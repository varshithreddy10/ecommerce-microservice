package com.ecom.customerapi.controller;


import com.ecom.customerapi.dto.CustomerDto;
import com.ecom.customerapi.repository.CustomerRepository;
import com.ecom.customerapi.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController
{

    @Autowired
    private CustomerService customerservice;

    @Autowired
    private CustomerRepository customerrepo;

    /**
     * Create a new customer
     */
    @PostMapping("/add/customer")
    //@PreAuthorize("hasAnyRole('SELLER','ADMIN','USER') and hasAuthority('USER_CREATE')")
    public ResponseEntity<CustomerDto> addNewCustomer(@RequestBody CustomerDto customerdto)
    {
        log.info("NATALIE control entered the addnewcustomer with the customerdto "+customerdto);
        System.out.println("========================================================================================================================================================================");
        CustomerDto customersaved = customerservice.addNewCustomerserv(customerdto);

        return new ResponseEntity<>(customersaved, HttpStatus.CREATED);
    }

    /**
     * Get all customers (ADMIN only)
     */
    @GetMapping("/getall/customers")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('USER_VIEW_ALL')")
    public ResponseEntity<List<CustomerDto>> getallCustomers()
    {

        List<CustomerDto> allcustomers = customerservice.getAllCustomers();

        return new ResponseEntity<>(allcustomers, HttpStatus.CREATED);
    }

    /**
     * Get logged-in customer details
     */
    @GetMapping("/get/logged/customerdetails")
    @PreAuthorize("hasAnyRole('ADMIN','USER') and hasAuthority('USER_VIEW')")
    public ResponseEntity<CustomerDto> getCustomerDetails()
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = (String) authentication.getPrincipal();
        Long customerId = Long.valueOf(userId);

        CustomerDto onecustomer = customerservice.getCustomerByIdserv(customerId);

        return new ResponseEntity<>(onecustomer, HttpStatus.CREATED);
    }

    /**
     * Update customer (ADMIN only)
     */
    @PutMapping("/get/category/{customerId}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('USER_UPDATE')")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDto customerdto) {
        CustomerDto onecustomer =
                customerservice.updateCustomerServ(customerId, customerdto);

        return new ResponseEntity<>(onecustomer, HttpStatus.CREATED);
    }

    /**
     * Delete customer (ADMIN only)
     */
    @DeleteMapping("/delete/category/{customerId}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('USER_DELETE')")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId)
    {
        String message =
                customerservice.deleteCustomerServ(customerId);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    /**
     * Get customer by ID (ADMIN only)
     */
    @GetMapping("/get/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('USER_VIEW')")
    public ResponseEntity<CustomerDto> getCustomerDetailsByIdByAdmin(@PathVariable Long customerId)
    {
        CustomerDto onecustomer = customerservice.getCustomerByIdserv(customerId);

        return new ResponseEntity<>(onecustomer, HttpStatus.CREATED);
    }
}
