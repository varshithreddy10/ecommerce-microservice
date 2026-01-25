package com.ecommerce.authuser.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetModelMapper
{
    @Bean
    public ModelMapper getmyModelMapper()
    {
        return new ModelMapper();
    }
}
