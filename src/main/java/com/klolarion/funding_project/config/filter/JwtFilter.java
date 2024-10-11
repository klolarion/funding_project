//21. JWT를 검증, 해당 필터를 통해 요청 쿠키에 JWT가 존재하는 경우 JWT를 검증하고 강제로SecurityContextHolder에 세션을 생성한다.
// (이 세션은 STATLESS 상태로 관리되기 때문에 해당 요청이 끝나면 소멸 된다.)
package com.klolarion.funding_project.config.filter;

import com.klolarion.funding_project.dto.oauth2.CustomOAuth2User;
import com.klolarion.funding_project.dto.oauth2.MemberDTO;
import com.klolarion.funding_project.util.token.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 특정 경로(/api 등)는 인증 없이도 접근 가능하도록 예외 처리
        if (request.getServletPath().contains("/api")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 access_token 토큰 추출
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            System.out.println("No cookies found");
            filterChain.doFilter(request, response);
            return;
        }

        String access = null;
        for (Cookie cookie : cookies) {
            if ("access_token".equals(cookie.getName())) {
                access = cookie.getValue();
                break;
            }
        }

        // access_token 쿠키가 없으면 필터 체인 진행
        if (access == null) {
            System.out.println("Token not found in cookies");
            filterChain.doFilter(request, response);
            return;
        }

        String token = access;

        // 토큰 만료 여부 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("Token expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 유효한 경우, 토큰에서 필요한 정보 추출
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        Long memberId = jwtUtil.getMemberId(token);

        // 회원 정보를 담은 DTO 생성
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setProvider(username);
        memberDTO.setRole(role);
        memberDTO.setMemberId(memberId);

        // CustomOAuth2User에 회원 정보 세팅
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDTO);

        // Spring Security 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // SecurityContextHolder에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}