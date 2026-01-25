package com.ecommerce.authuser.springsecurity.securityservice;


import com.ecommerce.authuser.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JwtService
{
   private static String jwtsecret = "ads9f6askj3h4k1hf86asdfiahkjh34a789s6df89ayshkjh3jkh786adsf78ay";

   private SecretKey getJwtSecret()
   {
       log.info("getting the secretkey");
       return Keys.hmacShaKeyFor(jwtsecret.getBytes(StandardCharsets.UTF_8));
   }

    public String generateAccessToken(UserEntity user)
    {


    /*    // Convert GrantedAuthority → List<String>
        List<String> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
                */

        List<String> roles = user.getRoles()
                .stream()
                .map(role -> "ROLE_" + role.name())
                .toList();

        List<String> authorities = new ArrayList<>(user.getAuthorities());



        return Jwts.builder()
                .subject(user.getCustomerId().toString())
                .claim("email", user.getEmail())
                .claim("rolesset", user.getRoles())
                .claim("rolesstring",roles)
                .claim("authorities", authorities)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getJwtSecret())
                .compact();
    }
}
