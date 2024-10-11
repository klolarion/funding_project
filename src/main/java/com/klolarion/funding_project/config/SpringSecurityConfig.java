package com.klolarion.funding_project.config;

import com.klolarion.funding_project.config.filter.JwtFilter;
import com.klolarion.funding_project.service.oAuth.CustomOAuth2UserService;
import com.klolarion.funding_project.util.oauth2.CustomLogoutSuccessHandler;
import com.klolarion.funding_project.util.oauth2.CustomSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
//@EnableWebSecurity(debug = true) //http관련 상세 디버깅용
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    @Value("${spring.base-url}")
    private String baseUrl;


    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final JwtFilter jwtFilter;

    private static final String[] PUBLIC_API_URL = {
            "/api/f1/**",
            "/oauth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private static final String[] ADMIN_API_URL = {
            "/admin/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        // CSRF 보호 비활성화
        http.csrf(AbstractHttpConfigurer::disable);
        //세션 비활성화
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //경로 관리
        http.authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().permitAll()
                        .requestMatchers(PUBLIC_API_URL).permitAll()
                        .requestMatchers(ADMIN_API_URL).hasRole("ADMIN")
                        .anyRequest().authenticated()
        );

        //From 로그인 disable
        http.formLogin(AbstractHttpConfigurer::disable);

        //HTTP Basic 인증 disable
        http.httpBasic(AbstractHttpConfigurer::disable);

        //소셜인증 관리
        http.oauth2Login(oauth2 -> oauth2
                        .loginPage("http://localhost:5173/social") // 프론트엔드의 로그인 페이지로 설정
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // OAuth2User 사용 설정
                        )
                        .successHandler(customSuccessHandler)
                );

        //로귿아웃 설정
        http.logout(logout -> logout
                        .logoutSuccessUrl("http://localhost:5173")
                        .logoutSuccessHandler(customLogoutSuccessHandler)  // 커스텀 로그아웃 성공 핸들러 적용
                        .invalidateHttpSession(true)  // 세션 무효화
                        .clearAuthentication(true)

                        .deleteCookies("JSESSIONID", "access_token", "refresh_token")
                );

        // 사용자 정의 인증 프로바이더 설정
        http.authenticationProvider(customAuthenticationProvider);

        //필터 추가
        http.addFilterBefore(jwtFilter, OAuth2LoginAuthenticationFilter.class);

        //인증 예외처리
        http.exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
                                    response.setCharacterEncoding("utf-8");
                                    response.setContentType("text/html; charset=UTF-8");
                                    response.getWriter().write("인증되지 않은 사용자입니다.");
                                }));

//                                .accessDeniedHandler((request, response, accessDeniedException) -> { //404도 같이 처리
//                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                                    response.setCharacterEncoding("utf-8");
//                                    response.setContentType("text/html; charset=UTF-8");
//                                    response.getWriter().write("잘못된 주소 혹은 권한이 없는 사용자입니다.");
//                                })
//                );
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
