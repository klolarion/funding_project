package com.klolarion.funding_project.util.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 사용자가 로그아웃을 하면 토큰을 지우고, 특정 페이지로 보내주는 메서드

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 액세스 토큰 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("access_token", null); // Authorization 라는 쿠키를 만들고, 값을 null로 설정해 쿠키를 무효화
        accessTokenCookie.setMaxAge(0); // 쿠키 유효기간을 0으로 설정해 만료시킴 (쿠키 즉시 삭제)
        accessTokenCookie.setPath("/"); // 모든 경로에서 쿠키가 삭제됨

        // 리프레시 토큰 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setMaxAge(0); // 쿠키 만료
        refreshTokenCookie.setPath("/");

        // 쿠키를 응답에 추가하여 삭제 처리
        response.addCookie(accessTokenCookie); // 브라우저에서 쿠키 삭제
        response.addCookie(refreshTokenCookie);

        // 로그아웃 후 리다이렉트할 URL 설정
        response.sendRedirect("http://localhost:5173"); // 로그아웃 후, 지정한 프론트 페이지로 이동
    }
}