package com.product.api.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class modelmapperconfig
{
    @Bean
    public ModelMapper getmodelmapper()
    {
        return new ModelMapper();
    }

}
