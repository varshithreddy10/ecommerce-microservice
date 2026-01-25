package com.ecom.customerapi.service;

import com.ecom.customerapi.dto.AddressDto;
import com.ecom.customerapi.entity.AddressEntity;
import com.ecom.customerapi.entity.CustomerEntity;
import com.ecom.customerapi.exception.ResourceNotFoundException;
import com.ecom.customerapi.repository.AddressRepository;
import com.ecom.customerapi.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService
{
    @Autowired
    private CustomerRepository customerrepo;

    @Autowired
    private ModelMapper modelmapper;

    @Autowired
    private AddressRepository addressrepo;

    @Override
    public AddressDto addNewAddress(AddressDto addressdto, Long customerId)
    {
        CustomerEntity customerentity = customerrepo.findById(customerId)
                .orElseThrow(()-> new ResourceNotFoundException("customer","customerId",""+customerId));

        AddressEntity addressentity = modelmapper.map(addressdto , AddressEntity.class);
        addressentity.setCustomer(customerentity);

        AddressEntity savedaddress = addressrepo.save(addressentity);
        return modelmapper.map(savedaddress , AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddresses()
    {
        List<AddressEntity> alladdressentity = addressrepo.findAll();
        List<AddressDto> alladdressdtos = alladdressentity.stream()
                .map(individualaddressentity -> modelmapper.map(individualaddressentity , AddressDto.class))
                .toList();

        return alladdressdtos;
    }

    @Override
    public List<AddressDto> getAllAddressofCustomerByIdServ(Long customerId)
    {
        CustomerEntity customerentity = customerrepo.findById(customerId)
                .orElseThrow(()-> new ResourceNotFoundException("customer","customerId",""+customerId));

        List<AddressEntity> alladdressentityofCustomer = addressrepo.findByCustomer_CustomerId(customerId);
        List<AddressDto> alladdressdtosofCustomer = alladdressentityofCustomer.stream()
                .map(individualaddressentity -> modelmapper.map(individualaddressentity , AddressDto.class))
                .toList();

        return alladdressdtosofCustomer;
    }

    @Override
    public String deleteCustomerAddressServ(Long addressId)
    {
        AddressEntity addressentity = addressrepo.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("address","addressId",""+addressId));

        addressrepo.deleteById(addressId);
        return "success";
    }

    @Override
    public AddressDto updateAddress(AddressDto addressdto, Long addressId)
    {
        AddressEntity addressentity = addressrepo.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("address","addressId",""+addressId));

        addressentity.setHno(addressdto.getHno());
        addressentity.setCity(addressdto.getCity());
        addressentity.setState(addressdto.getState());
        addressentity.setStreet(addressdto.getStreet());
        addressentity.setAddrType(addressdto.getAddrType());
        addressentity.setDeleteSw(addressdto.getDeleteSw());
        addressentity.setZipCode(addressdto.getZipCode());

        AddressEntity savedaddress = addressrepo.save(addressentity);

        return modelmapper.map(savedaddress , AddressDto.class);
    }


}
