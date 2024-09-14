package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/f1/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {
    private final MemberServiceImpl memberService;

    @GetMapping("/")
    public void login(){

    }

    @PostMapping("/")
    public void socialAuth(){

    }

    @PostMapping("/check-account")
    public ResponseEntity<?> checkAccount(@RequestBody Map<String, String> request) {
        String account = request.get("account");
        String provider = memberService.getProviderInfo(account);

        if (provider != null) {
            // 소셜 로그인 제공자 반환
            return ResponseEntity.ok(Collections.singletonMap("provider", provider));
        } else {
            // 등록되지 않은 계정의 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("등록된 소셜 로그인 제공자가 없습니다.");
        }
    }


    @GetMapping("/register")
    public ResponseEntity<?> register(@AuthenticationPrincipal OAuth2User principal, Model model, OAuth2AuthenticationToken token) {
        if (principal != null) {
            String registrationId = token.getAuthorizedClientRegistrationId();
            System.out.println("API regiID: " + registrationId);

            Member member = null;
            if ("google".equals(registrationId)) {
                // 구글 로그인 처리
                member = memberService.saveOrUpdateUserGoogle(principal);
            } else if ("naver".equals(registrationId)) {
                // 네이버 로그인 처리
                member = memberService.saveOrUpdateUserNaver(principal);
            } else if ("kakao".equals(registrationId)) {
                // 카카오 로그인 처리
                member = memberService.saveOrUpdateUserKakao(principal);
            } else {
                // 다른 소셜 로그인이 있을 경우를 대비한 처리
                throw new IllegalArgumentException("Unsupported provider: " + registrationId);
            }

            // 회원가입 완료 후 메인 페이지로 리다이렉트
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "http://localhost:5173");  // 메인 페이지로 리다이렉트
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        // 인증되지 않은 상태면 로그인 페이지로 리다이렉트
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "http://localhost:5173/login?error=true");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }




}
