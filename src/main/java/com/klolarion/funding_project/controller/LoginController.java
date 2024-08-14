package com.klolarion.funding_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klolarion.funding_project.dto.LoginDto;
import com.klolarion.funding_project.dto.OtpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/f1")
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private RestTemplate restTemplate;

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @GetMapping("/otp")
    public String otp(Model model) {
        String account = (String) model.getAttribute("account");
        model.addAttribute("account", account);
        return "otp";
    }

    @PostMapping("/loginPost")
    public String loginPost(@ModelAttribute LoginDto loginDto, RedirectAttributes redirectAttributes) {
        // 외부 서버로 요청을 보내기 위해 RestTemplate 사용
        String externalUrl = "https://almagest-auth.com/api/v1/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<LoginDto> requestEntity = new HttpEntity<>(loginDto, headers);

        // 외부 서버로 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(externalUrl, HttpMethod.POST, requestEntity, String.class);

        // 응답에서 필요한 데이터를 추출하여 리다이렉트할 때 사용할 수 있습니다.
        // 예: JWT 토큰, 사용자 정보 등
        // 여기서는 account를 다음 페이지로 넘깁니다.
        redirectAttributes.addFlashAttribute("account", loginDto.getAccount());

        return "redirect:/f1/otp";
    }

    @PostMapping("/otpPost")
    public String otpPost(@ModelAttribute OtpDto otpDto) {
        String externalUrl = "https://almagest-auth.com/api/v1/req";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<OtpDto> requestEntity = new HttpEntity<>(otpDto, headers);

        // 외부 서버로 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(externalUrl, HttpMethod.POST, requestEntity, String.class);

        // OTP 인증이 성공했다면 사용자를 마이페이지로 리다이렉트합니다.
        return "redirect:/myPage";
    }
}

