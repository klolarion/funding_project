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
                        .requestMatchers("/f1/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll() // 특정 경로는 모든 사용자에게 허용
                        .requestMatchers("/f0").hasRole("ADMIN")
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .authenticationProvider(customAuthenticationProvider) // 사용자 정의 인증 프로바이더 설정
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class) // 사용자 정의 필터 추가
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // 권한 문제가 발생했을 때 호출
                            response.setStatus(403);
                            response.setCharacterEncoding("utf-8");
                            response.setContentType("text/html; charset=UTF-8");
                            response.getWriter().write("권한이 없는 사용자입니다.");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 인증문제가 발생했을 때 호출
                            response.setStatus(401);
                            response.setCharacterEncoding("utf-8");
                            response.setContentType("text/html; charset=UTF-8");
                            response.getWriter().write("인증되지 않은 사용자입니다.");
                        })
                );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
