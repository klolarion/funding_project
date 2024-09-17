package com.klolarion.funding_project.config;

import com.klolarion.funding_project.util.OAuth.OAuth2MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
//@EnableWebSecurity(debug = true) //http관련 상세 디버깅용
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    @Value("${spring.base-url}")
    private String baseUrl;


    private final OAuth2MemberService oAuth2MemberService;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


//        .csrf((csrf) -> csrf
//                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository()) // (Default)
//                // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // JavaScript Application | Cookie | JSON
//        )

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) //WebMvcConfigurer의 설정을 가져온다
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안 함
                )
                .authorizeHttpRequests(authorize -> authorize
                                .anyRequest().permitAll()
//                        .requestMatchers(
//                                "/api/**",
//                                "/index/**",
//                                "/login/**",
//                                "/register/**",
//                                "/oauth/**",
//                                "/v3/api-docs/**",
//                                "/swagger-ui/**").permitAll() // 특정 경로는 모든 사용자에게 허용
//                                // .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("http://localhost:5173/social") // 프론트엔드의 로그인 페이지로 설정
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2MemberService) // OAuth2User 사용 설정
                        )
                        .defaultSuccessUrl("http://localhost:5173", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("http://localhost:5173")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
                .authenticationProvider(customAuthenticationProvider) // 사용자 정의 인증 프로바이더 설정
//                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(exceptionHandling -> 예외처리
//                        exceptionHandling
//                                .authenticationEntryPoint((request, response, authException) -> {
//                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                                    response.setCharacterEncoding("utf-8");
//                                    response.setContentType("text/html; charset=UTF-8");
//                                    response.getWriter().write("인증되지 않은 사용자입니다.");
//                                })
//                                .accessDeniedHandler((request, response, accessDeniedException) -> { //404도 같이 처리
//                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                                    response.setCharacterEncoding("utf-8");
//                                    response.setContentType("text/html; charset=UTF-8");
//                                    response.getWriter().write("잘못된 주소 혹은 권한이 없는 사용자입니다.");
//                                })
//                )
        ;
        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
