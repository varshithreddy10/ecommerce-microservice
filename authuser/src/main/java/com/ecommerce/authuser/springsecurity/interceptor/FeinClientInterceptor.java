package com.ecommerce.authuser.springsecurity.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class FeinClientInterceptor implements RequestInterceptor
{

    @Override
    public void apply(RequestTemplate requestTemplate)
    {
            log.info("NATALIE control entered the FeinClientInterceptor");

            requestTemplate.header("X-User-Id", "50");

            List<String> authoritiesOfUser = List.of("ROLE_USER" ,"ROLE_ADMIN","ROLE_SELLER");


            //  Joining authorities using comma
            String authoritiesHeader = String.join(",", authoritiesOfUser);
            log.info("NATALIE converting Set<String> to string "+authoritiesHeader);

            // Adding header
            requestTemplate.header("X-User-Authorities", authoritiesHeader);

        //requestTemplate.header("X-User-Create", "CREATING_NEW_USER");
    }
}
