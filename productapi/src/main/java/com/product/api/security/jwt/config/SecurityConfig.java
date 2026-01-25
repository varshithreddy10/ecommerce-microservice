package com.product.api.security.jwt.config;

import com.product.api.security.jwt.filter.GatewayAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, GatewayAuthenticationFilter gatewayAuthenticationFilter) throws Exception
    {
        log.info("NATALIE control entered securityFilterChain ");
            http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                                           sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(gatewayAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }
}
