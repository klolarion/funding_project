package com.klolarion.funding_project.config.filter;

import com.klolarion.funding_project.util.token.JwtProperties;
import com.klolarion.funding_project.util.token.JwtTokenProvider;
import com.klolarion.funding_project.service.TokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenServiceImpl tokenServiceImpl;
    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //토큰인증없이 넘어갈 경로 설정
        if (request.getServletPath().contains("/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 액세스 토큰 추출 및 검증
        String accessToken = tokenServiceImpl.getTokenFromCookie(request, "accessToken");

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            // 유효한 액세스 토큰이 있는 경우 인증 정보 설정
            Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // 액세스 토큰이 없거나 유효하지 않은 경우 리프레시 토큰으로 새 액세스 토큰 발급
            String refreshToken = tokenServiceImpl.getTokenFromCookie(request, "refreshToken");
            if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                Long userId = jwtTokenProvider.getUserId(refreshToken);

                // 설정된 만료 시간을 사용하여 새로운 액세스 토큰 생성
                String newAccessToken = jwtTokenProvider.createAccessToken(userId);

                // HttpOnly 쿠키에 새로운 액세스 토큰 저장, 설정 파일의 만료 시간 사용
                tokenServiceImpl.setHttpOnlyCookie(response, "accessToken", newAccessToken, (int) jwtProperties.getAccessTokenRemainSeconds());

                // 새로운 액세스 토큰으로 인증 정보 설정
                Authentication auth = jwtTokenProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

}

