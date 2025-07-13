package com.kim.SpringStudy.Security;

import com.kim.SpringStudy.JWT.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
        // 비번 해싱 함수를 정의
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());// csrf 잠시 꺼두기
        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/**").permitAll() //아무나 접속 (로그인 해제)
        );
        //내가 만든 Jwt 필터 사용
        http.addFilterBefore(new JwtFilter() , ExceptionTranslationFilter.class);



        //세션 생성하지 말아주세요
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );


        //폼으로 로그인 하겠다.
//        http.formLogin((formLogin) -> formLogin.loginPage("/login")
//                .defaultSuccessUrl("/") //index로 이동

        //);
        //로그아웃
        http.logout(logout -> logout.logoutUrl("/logout"));
        return http.build();
    }


}

//bean(스프링이 뽑은 객체)으로 만들어서 디펜던시 인젝션 수행하게끔 할 수 있음.