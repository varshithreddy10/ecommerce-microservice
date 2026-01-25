package com.ecommerce.authuser.springsecurity.securityservice;


import com.ecommerce.authuser.Exception.CustomerIdNotMatchException;
import com.ecommerce.authuser.Exception.ResourceNotFoundException;
import com.ecommerce.authuser.dto.*;
import com.ecommerce.authuser.entity.UserEntity;

import com.ecommerce.authuser.feignclients.Customer_Api_Feignclient;
import com.ecommerce.authuser.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthService
{
    @Autowired
    private UserRepository userrepo;

    @Autowired
    private ModelMapper modelmapper;

    @Autowired
    private PasswordEncoder passwordencoder;

    @Autowired
    private JwtService jwtservice;

    @Autowired
    private Customer_Api_Feignclient customerfeign;


    public Boolean creatingCustomerInCustomerService(UserEntity savedUser)
    {
        boolean result;
        log.info("NATALIE control entered the creatingCustomerInCustomerService with userentity = "+savedUser);

        CustomerCreateDto customercreatedto = modelmapper.map(savedUser , CustomerCreateDto.class);
        log.info("NATALIE converting the UserEntity to CustomerCreateDto = "+customercreatedto);

        try
        {
            CustomerCreateDto createdcustomer = customerfeign.createCustomer(customercreatedto);
            log.info("NATALIE saved customer in the customer-service is = "+createdcustomer);

            if(!savedUser.getCustomerId().equals(createdcustomer.getCustomerId()))
            {
                throw new CustomerIdNotMatchException("customerId's in the auth-service = "+savedUser.getCustomerId()+" and the Customer-service = "+createdcustomer.getCustomerId()+" doesnot match");
            }
            else
            {
                log.info("both the ids are matched savedUser.getCustomerId() = "+savedUser.getCustomerId()+" == createdcustomer.getCustomerId() = "+createdcustomer.getCustomerId());
            }

            result=true;
        }
        catch(Exception e)
        {

            result = false;
            log.error("NATALIE exception occured while creating customer in customer-service "+e.getMessage());
            userrepo.deleteById(savedUser.getCustomerId());
            log.info("creation of the user is failed in customer-service so we are deleting the user-entity from the database ");
        }

        return result;

    }

    public UserDto signUp(SignUpDto signUpDto)
    {
        log.info("control entered the signup() method with signupdto as "+signUpDto);
        Optional<UserEntity> user = userrepo.findByEmail(signUpDto.getEmail());
        if(user.isPresent())
        {
            throw new ResourceNotFoundException("User with email already exits "+ signUpDto.getEmail());
        }

        UserEntity toBeCreatedUser = modelmapper.map(signUpDto, UserEntity.class);
        toBeCreatedUser.setPassword(passwordencoder.encode(toBeCreatedUser.getPassword()));

        log.info("before saving userentity = "+toBeCreatedUser);

        UserEntity savedUser = userrepo.save(toBeCreatedUser);

        log.info("execution of the signup is completed and the saved user is "+savedUser);

        /*
           id is generated after saving in the database so saving the user in auth-service repo
           after saving extract the user from database and send them to the customer-service to create a new user
         */

        if(creatingCustomerInCustomerService(savedUser)==false)
        {
            return null;
        }

        return modelmapper.map(savedUser, UserDto.class);
    }

    public LoginResponseDto login(LoginDto dto) {

        UserEntity user = userrepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordencoder.matches(dto.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtservice.generateAccessToken(user);

        return new LoginResponseDto(user.getCustomerId(), token);
    }






}
