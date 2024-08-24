package com.klolarion.funding_project.config.filter;

import com.klolarion.funding_project.config.CustomUserDetailsService;
import com.klolarion.funding_project.domain.entity.CustomUserDetails;
import com.klolarion.funding_project.util.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //토큰인증없이 넘어갈 경로
        if (request.getServletPath().contains("/f1")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 인증서버 csrf토큰 -> 중복로그인 방지용으로 교체예정
        // samsite=lax 설정으로 csrf 대응

//        //csrf토큰추출
//        final String csrfToken = request.getHeader("almagest_csrf");
//
//        //헤더에 csrf토큰이 없으면 거부
//        if (csrfToken == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }


        // 프론트서버와 연결시 구현
        //httpOnly쿠키에서 access_token/refresh_token 추출
        Cookie[] cookies = request.getCookies();
        String access = null;
        String refresh = null;
        try {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    access = cookie.getValue();
                    continue;
                }
                if ("refresh_token".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    continue;
                }
                if(access != null && refresh != null){
                    break;
                }
            }
        } catch (Exception e) {
            log.error("쿠키 없어!!!!");
        }

        final String account;
        try {
            account = tokenService.extractMemberAccount(access);//jwt토큰에서 계정명 추출
            //추출된 계정명인 null이 아니고 보안컨텍스트의 인증정보가 null인경우 추출된 계정명으로 사용자정보를 db에서 불러온다.
            //보안컨텍스트에 인증정보가 존재하면 넘어간다.
            //불러온 사용자정보를 UsernamePasswordAuthenticationToken에 넣고 보안컨텍스트에 등록한다.

            if (account != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                //member객체 들어가있음
                CustomUserDetails userDetails = this.customUserDetailsService.loadUserByUsername(account);

                //토큰검증
                if (tokenService.isMemberTokenValid(access, userDetails)) {
                    //UsernamePasswordAuthenticationToken은 추후 인증이 끝나고 SecurityContextHolder.getContext()에 등록될 Authentication 객체이다
                    // 사용자 정보를 포함한 UsernamePasswordAuthenticationToken 생성
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,  // UserDetails 객체 자체를 넣음
                            null,  // 자격 증명은 null로 설정
                            userDetails.getAuthorities()  // 사용자의 권한 설정
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.error("인증 오류");
        }

        filterChain.doFilter(request, response);
    }

}

