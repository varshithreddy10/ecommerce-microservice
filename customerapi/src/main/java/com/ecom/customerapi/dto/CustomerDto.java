package com.ecom.customerapi.dto;

import com.ecom.customerapi.enums.Role;
import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CustomerDto
{
    private Long customerId;
    private String name;
    private String email;
    private String phoneNo;

    private Set<Role> roles;

    private LocalDate dateCreated;
    private LocalDate lastUpdated;
}
