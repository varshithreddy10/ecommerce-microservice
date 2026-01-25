package com.ecommerce.authuser.dto;


import com.ecommerce.authuser.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class CustomerCreateDto
{
    private Long customerId;
    private String name;
    private String email;
    private String phoneNo;

    private Set<Role> roles;

}
