package com.ecom.customerapi.entity;



import com.ecom.customerapi.enums.Role;

import com.ecom.customerapi.springsecurity.config.PermissionMapping;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name = "customer")
public class CustomerEntity {

    @Id
    private Long customerId;

    private String name;
    private String email;
    private String phoneNo;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @CreationTimestamp
    @Column(name = "date_created", updatable = false)
    private LocalDate dateCreated;

    @UpdateTimestamp
    @Column(name = "last_updated", insertable = false)
    private LocalDate lastUpdated;

    public Set<String> getAuthorities() {
        Set<String> authorities = new HashSet<>();

        for (Role role : roles) {
            authorities.add("ROLE_" + role.name());
            authorities.addAll(PermissionMapping.getAuthoritiesForRole(role));
        }
        return authorities;
    }
}
