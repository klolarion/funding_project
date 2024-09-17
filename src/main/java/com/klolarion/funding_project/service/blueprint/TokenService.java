package com.klolarion.funding_project.service.blueprint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    // HttpOnly 쿠키에 토큰 저장
    void setHttpOnlyCookie(HttpServletResponse response, String name, String token, int maxAge);

    // 요청에서 쿠키로부터 토큰 가져오기
    String getTokenFromCookie(HttpServletRequest request, String name);

    // 쿠키 삭제
    void deleteCookie(HttpServletResponse response, String name);
}
