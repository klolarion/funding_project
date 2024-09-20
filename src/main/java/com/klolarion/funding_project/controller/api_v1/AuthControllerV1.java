package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.dto.auth.RegisterDto;
import com.klolarion.funding_project.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/f1/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthControllerV1 {
    private final AuthServiceImpl authService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        boolean result = authService.register(registerDto);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
    }

    @PostMapping("/check-account/{account}")
    public ResponseEntity<?> checkAccount(@PathVariable String account) {
        String provider = authService.checkAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body(provider);
    }

    @GetMapping("/account/{account}")
    public ResponseEntity<?> lookAccount(@PathVariable String account) {
        authService.lookAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body("계정 사용 가능");
    }

    @GetMapping("/tel/{tel}")
    public ResponseEntity<?> lookTel(@PathVariable String tel) {
        authService.lookTel(tel);
        return ResponseEntity.status(HttpStatus.OK).body("전화번호 사용 가능");
    }


}
