package com.klolarion.funding_project.config;

import com.klolarion.funding_project.config.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {


    private final JwtFilter authenticationFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안 함
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/opt", "/v3/api-docs/**", "/swagger-ui/**").permitAll() // 특정 경로는 모든 사용자에게 허용
                        .anyRequest().permitAll() // 
                )
                .authenticationProvider(customAuthenticationProvider) // 사용자 정의 인증 프로바이더 설정
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class) // 사용자 정의 필터 추가
        ;
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
