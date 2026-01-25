package com.ecommerce.apigateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class JwtService {


    private String jwtSecret="ads9f6askj3h4k1hf86asdfiahkjh34a789s6df89ayshkjh3jkh786adsf78ay";

    private SecretKey getKey()
    {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims validateAndGetClaims(String token)
    {
        log.info("NATALIE control entered the validateAndGetClaims");
        Claims claims =  Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        log.info("NATALIE execution of  validateAndGetClaims is completed and the claims are = "+claims);
        return claims;
    }
}

