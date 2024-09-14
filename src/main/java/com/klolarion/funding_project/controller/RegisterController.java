package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.auth.RegisterDto;
import com.klolarion.funding_project.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {
    private final MemberServiceImpl memberService;


    @GetMapping
    public String register(@AuthenticationPrincipal OAuth2User principal, Model model, OAuth2AuthenticationToken token) {
        if (principal != null) {
//            Member member = memberService.saveOrUpdateUserGoogle(principal);
            String registrationId = token.getAuthorizedClientRegistrationId();
            System.out.println("regiID: " + registrationId);

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

            model.addAttribute("member", member);
            // 인증 성공 시 회원 등록 후 바로 메인 페이지로 리다이렉트
            return "redirect:http://localhost:5173";
        }
        // 인증 실패 시 다시 로그인 페이지로. 오류 메시지 필요
        return "redirect:http://localhost:5173/login";
    }


    //후추 연동 고려
    @PostMapping
    public ResponseEntity<String> almagestRegister(@RequestBody RegisterDto registerDto) {
        try {
            memberService.save(registerDto);
            return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");
        }
    }

}
