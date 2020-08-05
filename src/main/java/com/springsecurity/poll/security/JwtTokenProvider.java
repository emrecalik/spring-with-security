package com.springsecurity.poll.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@NoArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${app.secret}")
    private String tokenSecret;

    @Value("${app.tokenExpirationInMs}")
    private String tokenExpirationInMs;

    public String generateToken(Authentication authentication) {

        MyUserPrincipal myUserPrincipal = (MyUserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Instant expirationDate = Instant.ofEpochMilli(now.getTime() + Long.parseLong(tokenExpirationInMs));

        return Jwts.builder()
                .setSubject(myUserPrincipal.getId().toString())
                .setIssuedAt(now)
                .setExpiration(Date.from(expirationDate))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public Long getIdFromToken(String authToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(authToken)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            log.debug(Arrays.toString(ex.getStackTrace()));
            return false;
        }
    }
}
