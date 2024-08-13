package com.klolarion.funding_project.config.filter;

import com.klolarion.funding_project.config.CustomUserDetailsService;
import com.klolarion.funding_project.domain.entity.CustomUserDetails;
import com.klolarion.funding_project.util.CacheHandler;
import com.klolarion.funding_project.util.RedisService;
import com.klolarion.funding_project.util.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisService redisService;
    private final CacheHandler cacheHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //토큰인증없이 넘어갈 경로
        if (request.getServletPath().contains("/f1")){
            filterChain.doFilter(request, response);
            return;
        }
        //admin 요청일 경우
//        else if (request.getServletPath().contains("/f0")){
//
//            //csrf토큰추출
//            final String csrfToken = request.getHeader("csrf");
//
//            //헤더에 csrf토큰이 없으면 거부
//            if (csrfToken == null) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            //csrf토큰이 redis에 저장된 토큰과 같은지 확인
//            try {
//                boolean csrfTokenValid = tokenService.isAdminCsrfTokenValid(csrfToken);
//            }catch (Exception e){
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            //httpOnly쿠키에서 access_token추출
//            Cookie[] cookies = request.getCookies();
//            String token = null;
//            try {
//                for (Cookie cookie : cookies) {
//                    if ("admin_token".equals(cookie.getName())) {
//                        token = cookie.getValue();
//                        break;
//                    }
//                }
//            }catch (Exception e){
//            }
//            final String jwt = token;
//            final String account;
//            try {
//                account = tokenService.extractAccount(jwt);//jwt토큰에서 계정명 추출
//
//                //추출된 계정명인 null이 아니고 보안컨텍스트의 인증정보가 null인경우 추출된 계정명으로 사용자정보를 db에서 불러온다.
//                //보안컨텍스트에 인증정보가 존재하면 넘어간다.
//                //불러온 사용자정보를 UsernamePasswordAuthenticationToken에 넣고 보안컨텍스트에 등록한다.
//
//                if (account != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                    CustomUserDetails userDetails = this.customUserDetailsService.loadUserByUsername(account);
//                    System.out.println(userDetails);
//
//                    //토큰검증
//                    if (tokenService.isAdminTokenValid(jwt, userDetails)) {
//                        //UsernamePasswordAuthenticationToken은 추후 인증이 끝나고 SecurityContextHolder.getContext()에 등록될 Authentication 객체이다
//                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                                userDetails.getUsername(),
//                                userDetails.getPassword(),
//                                userDetails.getAuthorities()
//                        );
//                        authToken.setDetails(
//                                new WebAuthenticationDetailsSource().buildDetails(request)
//                        );
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                    }
//                }
//            }catch (Exception e){
//            }
//
//            filterChain.doFilter(request, response);
//        }
//        //기타 모든 요청 (사용자)
//        else {
//
//            //csrf토큰추출
//            final String csrfToken = request.getHeader("csrf");
//
//            //헤더에 csrf토큰이 없으면 거부
//            if (csrfToken == null) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//

            //csrf토큰이 redis에 저장된 토큰과 같은지 확인

//
//            //httpOnly쿠키에서 access_token추출
//            Cookie[] cookies = request.getCookies();
//            String token = null;
//            try {
//                for (Cookie cookie : cookies) {
//                    if ("access_token".equals(cookie.getName())) {
//                        token = cookie.getValue();
//                        break;
//                    }
//                }
//            } catch (Exception e) {
//            }
//            final String jwt = token;
//            final String account;
//            try {
//                account = tokenService.extractMemberAccount(jwt);//jwt토큰에서 계정명 추출
//                //추출된 계정명인 null이 아니고 보안컨텍스트의 인증정보가 null인경우 추출된 계정명으로 사용자정보를 db에서 불러온다.
//                //보안컨텍스트에 인증정보가 존재하면 넘어간다.
//                //불러온 사용자정보를 UsernamePasswordAuthenticationToken에 넣고 보안컨텍스트에 등록한다.
//
//                if (account != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                    CustomUserDetails userDetails = this.customUserDetailsService.loadUserByUsername(account);
//
//                    //토큰검증
//                    if (tokenService.isMemberTokenValid(jwt, userDetails)) {
//                        //UsernamePasswordAuthenticationToken은 추후 인증이 끝나고 SecurityContextHolder.getContext()에 등록될 Authentication 객체이다
//                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                                userDetails.getUsername(),
//                                userDetails.getPassword(),
//                                userDetails.getAuthorities()
//                        );
//                        authToken.setDetails(
//                                new WebAuthenticationDetailsSource().buildDetails(request)
//                        );
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                    }
//                }
//            } catch (Exception e) {
//            }
//
//            filterChain.doFilter(request, response);
//        }

    }
}
