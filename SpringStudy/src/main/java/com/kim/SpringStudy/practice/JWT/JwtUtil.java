package com.kim.SpringStudy.practice.JWT;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {


    //JWT 만들어 주는 키
    static final SecretKey key =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                    "jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword"
            ));

    // JWT 만들어주는 함수
    public static String createToken(Authentication auth) {

        String username = auth.getName(); // 로그인한 사용자 username

        //권한 설정
        var authorities = auth.getAuthorities().stream().
                map(a -> a.getAuthority()).
                collect(Collectors.joining(","));

        String jwt = Jwts.builder()
                .claim("username", username) // 로그인 한 사람의 닉네임 가져옴
                .claim("authorities", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1일
                .signWith(key)
                .compact();
        return jwt;
    }

    // JWT 까주는 함수
    public static Claims extractToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        return claims;
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = extractToken(token);
        return claims.get("username", String.class);
    }

    public static String getAuthoritiesFromToken(String token) {
        Claims claims = extractToken(token);
        return claims.get("authorities", String.class);
    }





}
