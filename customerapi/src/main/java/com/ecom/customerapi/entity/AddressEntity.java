package com.ecom.customerapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customer_address")
public class AddressEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addrId;
    private String hno;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String addrType;
    private String deleteSw;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;
}
