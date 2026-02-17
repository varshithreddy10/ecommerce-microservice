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

        String path = exchange.getRequest().getURI().getPath();
        log.info("Incoming request path: " + path);

        // =====================================================
        // ✅ 1️⃣ ALLOW SWAGGER & AUTH WITHOUT JWT
        // =====================================================
        if (path.contains("/v3/api-docs") ||
            path.contains("/swagger-ui") ||
            path.contains("/swagger-ui.html") ||
            path.contains("/webjars") ||
            path.contains("/api/auth")) {

            log.info("Swagger/Auth endpoint detected. Skipping JWT validation.");
            return chain.filter(exchange);
        }

        // =====================================================
        // 🔐 2️⃣ CHECK JWT FOR ALL OTHER REQUESTS
        // =====================================================
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        log.info("JWT token = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer "))
        {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try
        {
            Claims claims = jwtService.validateAndGetClaims(token);

            String userId = claims.getSubject();
            String email = claims.get("email", String.class);

            List<String> rolesSet = claims.get("rolesset", List.class);
            List<String> rolesString = claims.get("rolesstring", List.class);
            List<String> authoritiesOfUser = claims.get("authorities", List.class);

            ServerWebExchange modifiedExchange =
                    exchange.mutate()
                            .request(req -> req
                                    .header("X-User-Id", userId)
                                    .header("X-User-RoleSet", String.join(",", rolesSet))
                                    .header("X-User-Roles", String.join(",", rolesString))
                                    .header("X-User-Authorities", String.join(",", authoritiesOfUser))
                            )
                            .build();

            return chain.filter(modifiedExchange);

        }
        catch (Exception e)
        {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    };
}


    public static class Config
    {

    }
}
