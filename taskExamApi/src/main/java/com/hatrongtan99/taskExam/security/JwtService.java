package com.hatrongtan99.taskExam.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${auth.jwt.secret-key}")
    private String secretKey;

    public String generateToken(UserPrincipal user) {
        List<SimpleGrantedAuthority> roles = (List<SimpleGrantedAuthority>) user.getAuthorities();
        String id = user.getId();
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("fullname", user.getFullname());
        claims.put("email", user.getEmail());
        claims.put("roles", roles);
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .claims(claims)
                .signWith(getSignKey())
                .compact();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimFunction) {
        return  claimFunction.apply(getAllClaims(token));
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
