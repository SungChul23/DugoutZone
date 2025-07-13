package com.kim.SpringStudy.JWT;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // 필터 등록

//JWT 필터 만드는데 요청마다 1회 실행 되게끔 exteds한 상황
public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,//IP , OS 정보 , 언어 등등
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        System.out.println(cookies[0].getName()); // 출력 jwt
        System.out.println(cookies[0].getValue()); // 출력 토큰값

        var jwtCookie = "";

        if (cookies == null) {
            filterChain.doFilter(request, response); // 다음필터 수행 해줘 없으면
            return;
        }

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("jwt")) { //모든 쿠키에서 jwt면 그 내용을 저장해주세요
                jwtCookie = cookies[i].getValue();
            }
        }
        System.out.println(jwtCookie);


        Claims claim;
        try {
            claim = JwtUtil.extractToken(jwtCookie);
        } catch (Exception e) {
            System.out.println("유효기간 만료되거나 이상함");
            filterChain.doFilter(request, response);
            return;
        }


        filterChain.doFilter(request, response);
    }

}
