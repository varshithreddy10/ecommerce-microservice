package com.ecom.orderapi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerDto
{
    private Long customerId;
    private String name;
    private String email;
    private String password;
    private String pwdUpdated;
    private String phoneNo;

    private LocalDate dateCreated;

    private LocalDate lastUpdated;
}
