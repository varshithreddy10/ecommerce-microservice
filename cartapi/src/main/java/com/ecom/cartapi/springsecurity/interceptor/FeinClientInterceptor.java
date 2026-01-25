package com.ecom.cartapi.springsecurity.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FeinClientInterceptor implements RequestInterceptor
{
    @Override
    public void apply(RequestTemplate requestTemplate)
    {
        log.info("NATALIE control entered the FeinClientInterceptor");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("NATALIE authentication object is = "+authentication);


        if(authentication != null)
        {
            Long userId = (Long)authentication.getPrincipal();

            requestTemplate.header("X-User-Id", userId.toString());
            log.info("NATALIE userId = "+userId);

            //Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            //  Converting authorities to Set<String>
            Set<String> authoritiesOfUser = authentication.getAuthorities().stream()
                                                                .map(GrantedAuthority::getAuthority)
                                                                .collect(Collectors.toSet());
            log.info("NATALIE converting Collection<? extends GrantedAuthority> authorities to Set<String> = "+authoritiesOfUser);

            //  Joining authorities using comma
            String authoritiesHeader = String.join(",", authoritiesOfUser);
            log.info("NATALIE converting Set<String> to string "+authoritiesHeader);

            // Adding header
            requestTemplate.header("X-User-Authorities", authoritiesHeader);


        }
    }
}
