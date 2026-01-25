package com.ecommerce.authuser.entity;


import com.ecommerce.authuser.enums.Role;
import com.ecommerce.authuser.springsecurity.securityconfig.PermissionMapping;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Slf4j
public class UserEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String name;
    private String email;
    private String phoneNo;

    private String password;


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @CreationTimestamp
    @Column(name="date_created", updatable = false)
    private LocalDate dateCreated;

    @UpdateTimestamp
    @Column(name="last_updated", insertable = false)
    private LocalDate lastUpdated;

    /**
     * Builds authorities as plain Strings:
     * ROLE_* + permission names
     */
    public Set<String> getAuthorities()
    {
        Set<String> authorities = new HashSet<>();

        for (Role individualrole : roles)
        {
            authorities.add("ROLE_" + individualrole.name());
            authorities.addAll(PermissionMapping.getAuthoritiesForRole(individualrole));
        }
        return authorities;
    }

    /*public Set<String> getAuthorities()
    {
        Set<String> authorities = new HashSet<>();
        roles.forEach(individualrole->{
            Set<String> permissions = PermissionMapping.getAuthoritiesForRole(individualrole);
            authorities.addAll(permissions);
            authorities.add("ROLE_"+individualrole);
        });

        return authorities;

    }*/

}
