package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.dto.LoginDto;
import com.klolarion.funding_project.dto.OtpDto;
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
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/otp")
    public String otp(@ModelAttribute Model model) {
        String account = (String) model.getAttribute("account");
        model.addAttribute("account", account);
        return "login";
    }

    @PostMapping("/loginPost")
    public String loginPost(@ModelAttribute LoginDto loginDto, RedirectAttributes redirectAttributes) {
        // 외부 서버로 요청을 보내기 위해 RestTemplate 사용
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("--------Login--------");
        System.out.println(loginDto.getAccount() +", " + loginDto.getPassword());

        // 외부 서버의 URL
        String externalUrl = "https://almagest-auth.com/api/v1/auth";

        // 요청 헤더 설정 (필요한 경우)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        redirectAttributes.addFlashAttribute("account", loginDto.getAccount());

        // 요청 엔터티 생성
        HttpEntity<LoginDto> requestEntity = new HttpEntity<>(loginDto, headers);

        // 외부 서버로 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(externalUrl, HttpMethod.POST, requestEntity, String.class);

        return "redirect:/f1/otp";
    }

    @PostMapping("/otpPost")
    public String otpPost(@ModelAttribute OtpDto otpDto) {

        System.out.println("------- Otp -----");
        System.out.println(otpDto.getAccount() + ", " + otpDto.getCode());

        // 외부 서버로 요청을 보내기 위해 RestTemplate 사용
        RestTemplate restTemplate = new RestTemplate();

        // 외부 서버의 URL
        String externalUrl = "https://almagest-auth.com/api/v1/req";

        // 요청 헤더 설정 (필요한 경우)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 엔터티 생성
        HttpEntity<OtpDto> requestEntity = new HttpEntity<>(otpDto, headers);

        // 외부 서버로 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(externalUrl, HttpMethod.POST, requestEntity, String.class);

        return "redirect:/myPage";
    }
}

