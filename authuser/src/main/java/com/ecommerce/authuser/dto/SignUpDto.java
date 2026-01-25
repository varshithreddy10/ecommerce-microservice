package com.ecommerce.authuser.dto;




import com.ecommerce.authuser.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto
{
    private Long customerId;
    private String name;
    private String email;
    private String phoneNo;

    private String password;

    private Set<Role> roles;

}
