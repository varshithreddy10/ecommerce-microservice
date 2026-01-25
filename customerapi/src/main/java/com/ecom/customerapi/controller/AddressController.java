package com.ecom.customerapi.controller;

import com.ecom.customerapi.dto.AddressDto;
import com.ecom.customerapi.dto.CustomerDto;
import com.ecom.customerapi.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController
{
    @Autowired
    private AddressService addressservice;

    @PostMapping("/add/address/{customerId}")
    public ResponseEntity<AddressDto> addNewCustomerAddress(@PathVariable Long customerId,@RequestBody AddressDto addressdto)
    {
        AddressDto addresssaved = addressservice.addNewAddress(addressdto , customerId);
        return new ResponseEntity<>(addresssaved , HttpStatus.CREATED);
    }

    @GetMapping("/getall/addresses")
    public ResponseEntity<List<AddressDto>> getAllAddress()
    {
        List<AddressDto> allAddresses = addressservice.getAllAddresses();
        return new ResponseEntity<>(allAddresses , HttpStatus.CREATED);
    }

    @GetMapping("/getall/addresses/{customerId}")
    public ResponseEntity<List<AddressDto>> getAllAddressofCustomerById(@PathVariable Long customerId)
    {
        List<AddressDto> allAddressesofcustomer = addressservice.getAllAddressofCustomerByIdServ(customerId);
        return new ResponseEntity<>(allAddressesofcustomer , HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/address/{addressId}")
    public ResponseEntity<String> deleteCustomerAddress(@PathVariable Long addressId)
    {
        String message = addressservice.deleteCustomerAddressServ(addressId);
        return new ResponseEntity<>(message , HttpStatus.CREATED);
    }

    @PutMapping("/update/address/{addressId}")
    public ResponseEntity<AddressDto> updateCustomerAddress(@PathVariable Long addressId,@RequestBody AddressDto addressdto)
    {
        AddressDto updatedAddress = addressservice.updateAddress(addressdto , addressId);
        return new ResponseEntity<>(updatedAddress , HttpStatus.CREATED);
    }



}
