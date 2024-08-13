package com.klolarion.funding_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klolarion.funding_project.dto.LoginDto;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/f1")
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/loginPost")
    public ResponseEntity<String> loginPost(@RequestBody LoginDto loginDto) {
        System.out.println("loginPost");
        try {
            // RestTemplate 설정 및 Jackson 메시지 컨버터 추가
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            // 외부 서버의 URL
            String externalUrl = "https://almagest-auth.com/api/v1/auth";

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 엔터티 생성
            HttpEntity<LoginDto> requestEntity = new HttpEntity<>(loginDto, headers);

            // JSON으로 변환된 DTO 객체 확인 (디버깅용)
            System.out.println("Sending JSON: " + new ObjectMapper().writeValueAsString(loginDto));

            // 외부 서버로 POST 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(externalUrl, HttpMethod.POST, requestEntity, String.class);

            // 외부 서버의 응답을 반환
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 발생 시 INTERNAL_SERVER_ERROR 상태와 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}