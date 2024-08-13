package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.dto.LoginDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/f1")
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/loginPost")
    public void loginPost(@ModelAttribute LoginDto loginDto) {
        // 외부 서버로 요청을 보내기 위해 RestTemplate 사용
        RestTemplate restTemplate = new RestTemplate();

        // 외부 서버의 URL
        String externalUrl = "https://almagest-auth.com/api/v1/auth";

        // 요청 헤더 설정 (필요한 경우)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 엔터티 생성
        HttpEntity<LoginDto> requestEntity = new HttpEntity<>(loginDto, headers);

        // 외부 서버로 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(externalUrl, HttpMethod.POST, requestEntity, String.class);
//
//        // 외부 서버의 응답을 처리 (필요한 경우)
//        if (response.getStatusCode().is2xxSuccessful()) {
//            // 성공적인 경우 처리 (예: 리다이렉트)
//            return "redirect:/success";  // 로컬 성공 페이지로 리다이렉트
//        } else {
//            // 실패한 경우 처리 (예: 에러 페이지)
//            model.addAttribute("error", "Login failed. Please try again.");
//            return "login";  // 다시 로그인 페이지로 돌아감
//        }
//        return "redirect:/f1/login";
    }

