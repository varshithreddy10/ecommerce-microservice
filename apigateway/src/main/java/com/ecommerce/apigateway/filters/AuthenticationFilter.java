package com.ecommerce.apigateway.filters;


import com.ecommerce.apigateway.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>
{
// .
    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService)
    {
        super(Config.class);
        this.jwtService = jwtService;
    }

   @Override
    public GatewayFilter apply(Config config)
    {
        log.info("PORTMAN control entered the AuthenticationFilter ");
        return (exchange, chain) -> {

            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            log.info("NATALIE jwttoken is = "+authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer "))
            {

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.info("NATALIE control entered the if-else and the message is "+exchange.getResponse().setComplete());
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            try
            {
                Claims claims = jwtService.validateAndGetClaims(token);
                log.info("NATALIE claims are  = "+claims);

                String userId = claims.getSubject();
                log.info("NATALIE userId is  = "+userId);

                String email = claims.get("email",String.class);
                log.info("NATALIE email = "+email);


                List<String> rolesSet = claims.get("rolesset", List.class);
                log.info("NATALIE rolesset are  = "+rolesSet);

                List<String> rolesString = claims.get("rolesstring", List.class);
                log.info("NATALIE rolesString are  = "+rolesString);

                List<String> athoritiesOfUser = claims.get("authorities", List.class);
                log.info("NATALIE athoritiesOfUser are  = "+athoritiesOfUser);

                ServerWebExchange modifiedExchange =
                        exchange.mutate()
                                .request(req -> req
                                        .header("X-User-Id", userId)
                                        .header("X-User-RoleSet",String.join(",",rolesSet))
                                        .header("X-User-Roles", String.join(",", rolesString))
                                        .header("X-User-Authorities", String.join(",", athoritiesOfUser))
                                )
                                .build();
                log.info("NATALIE ServerWebExchange modifiedExchange = "+modifiedExchange);

                return chain.filter(modifiedExchange);

            }
            catch (Exception e)
            {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.error("exception occured and the  1 error is "+e.getMessage());
                log.error("exception occured and the  2 error is "); //+exchange.getResponse().setComplete()

                return exchange.getResponse().setComplete();
            }
        };
    }

    public static class Config
    {

    }
}
