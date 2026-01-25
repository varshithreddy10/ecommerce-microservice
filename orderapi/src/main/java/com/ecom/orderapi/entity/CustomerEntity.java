package com.ecom.orderapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "customer")
public class CustomerEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String name;
    private String email;
    private String password;
    private String pwdUpdated;
    private String phoneNo;
    @CreationTimestamp
    @Column(name="date_created", updatable = false)
    private LocalDate dateCreated;

    @UpdateTimestamp
    @Column(name="last_updated", insertable = false)
    private LocalDate lastUpdated;
}
