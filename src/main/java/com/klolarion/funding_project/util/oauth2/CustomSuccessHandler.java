//19.OAuth2 로그인이 성공하면 실행될 성공 핸들러를 커스텀해서 내부에 JWT 발급 구현을 진행
package com.klolarion.funding_project.util.oauth2;


import com.klolarion.funding_project.dto.oauth2.CustomOAuth2User;
import com.klolarion.funding_project.util.token.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

//사용자가 소셜 인증 성공 시, 처리되는 메서드
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil; // JWT 토큰을 생성하고 처리하는 클래스를 주입

    public CustomSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    } // 생성자를 통해 JWTUtil 객체를 초기화

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 로그인한 사용자 정보 가져오기
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal(); // CustomOAuth2User에서 사용자 정보를 가져오기

        String username = customUserDetails.getNickName();
        Long memberId = customUserDetails.getMemberId();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 액세스 토큰과 리프레시 토큰 생성
        String accessToken = jwtUtil.createJwt(username, role, 60*60*60L, memberId); // 1시간 동안 유효한 액세스 토큰
        String refreshToken = jwtUtil.createRefreshToken(username, 60*60*24*7L, memberId); // 7일 동안 유효한 리프레시 토큰

        // 쿠키에 저장 (보안을 위해 Secure, HttpOnly 설정 가능)
        response.addCookie(createCookie("access_token", accessToken)); // 액세스 토큰 저장 (쿠키 방식으로 전달)
        response.addCookie(createCookie("refresh_token", refreshToken)); // 리프레시 토큰 저장

        response.sendRedirect("http://localhost:5173"); // 프론트측 특정 url에 리다이렉팅
    }

    private Cookie createCookie(String key, String value) { // 쿠키 만들기

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60); // 쿠키 살아있는 시간
        //cookie.setSecure(true); // http 통신에서만 사용
        cookie.setPath("/");
        cookie.setHttpOnly(true); // js가 해당 쿠키를 가져가지 못하게 설정
        return cookie;
    }
}

