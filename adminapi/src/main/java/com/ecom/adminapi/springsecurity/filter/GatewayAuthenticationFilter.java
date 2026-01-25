package com.ecom.adminapi.springsecurity.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GatewayAuthenticationFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        log.info("NATALIE control entered doFilterInternal");

        String userId = request.getHeader("X-User-Id");
        log.info("NATALIE control entered doFilterInternal");

        String authoritiesHeader = request.getHeader("X-User-Authorities");
        log.info("NATALIE control entered doFilterInternal");

        if (userId != null && authoritiesHeader != null)
        {
            /*
            List<SimpleGrantedAuthority> authorities =
                    Arrays.stream(authoritiesHeader.split(","))
                            .map(String::trim)
                            .map(SimpleGrantedAuthority::new)
                            .toList();
             */
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            String[] authorityArray = authoritiesHeader.split(",");

            for (String authority : authorityArray) {
                String cleaned = authority.trim();
                SimpleGrantedAuthority grantedAuthority =
                        new SimpleGrantedAuthority(cleaned);

                authorities.add(grantedAuthority);
            }


            log.info("NATALIE List<SimpleGrantedAuthority> authorities = "+authorities);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
            log.info("NATALIE authentication object created is = "+authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("NATALIE execution of doFilterInternal is completed and authentiction object in the securitycontextholder is ="+SecurityContextHolder.getContext().getAuthentication());

        }
        filterChain.doFilter(request, response);

    }
}
