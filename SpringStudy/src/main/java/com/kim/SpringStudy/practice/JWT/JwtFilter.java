package com.kim.SpringStudy.practice.JWT;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtCookie = "";

        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                jwtCookie = cookie.getValue();
                break;
            }
        }

        if (jwtCookie.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (cookies == null || jwtCookie.isEmpty()) {
            SecurityContextHolder.clearContext(); // 인증객체 클리어
            filterChain.doFilter(request, response);
            return;
        }


        try {
            Claims claim = JwtUtil.extractToken(jwtCookie);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            JwtUtil.getUsernameFromToken(jwtCookie),
                            null,
                            AuthorityUtils.commaSeparatedStringToAuthorityList(
                                    JwtUtil.getAuthoritiesFromToken(jwtCookie)
                            )
                    );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {
            System.out.println("유효기간 만료되거나 토큰 이상함");
        }

        filterChain.doFilter(request, response);
    }
}
