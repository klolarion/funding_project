package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.auth.RegisterDto;
import com.klolarion.funding_project.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String register(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            Member member = memberService.saveOrUpdateUserGoogle(principal);
            model.addAttribute("member", member);
            //인증 성공시 회원등록 후 바로 메인페이지로 리다이렉트
            return "redirect:/index";
        }
        //인증실패시 다시 로그엔페이지로. 오류메세지 필요
        return "redirect:/login";
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
