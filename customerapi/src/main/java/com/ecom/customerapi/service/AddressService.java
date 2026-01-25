package com.ecom.customerapi.service;

import com.ecom.customerapi.dto.AddressDto;

import java.util.List;

public interface AddressService
{
    public AddressDto addNewAddress(AddressDto addressdto, Long customerId);

    public List<AddressDto> getAllAddresses();

    public List<AddressDto> getAllAddressofCustomerByIdServ(Long customerId);

    public String deleteCustomerAddressServ(Long addressId);

    AddressDto updateAddress(AddressDto addressdto, Long addressId);
}
