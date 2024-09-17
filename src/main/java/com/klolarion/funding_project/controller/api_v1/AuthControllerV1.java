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
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){
        try {
            boolean result = authService.register(registerDto);
            if(result) {
                return ResponseEntity.status(HttpStatus.OK).body("");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    @PostMapping("/check-account/{account}")
    public ResponseEntity<?> checkAccount(@PathVariable String account) {
        String provider = authService.checkAccount(account);

        if (provider != null) {
            // 소셜 로그인 제공자
            return ResponseEntity.status(HttpStatus.OK).body(provider);
        } else {
            // 등록되지 않은 계정의 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("등록된 소셜 로그인 정보가 없습니다.");
        }
    }

    @GetMapping("/account/{account}")
    public ResponseEntity<?> lookAccount(@PathVariable String account){
        authService.lookAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping("/tel/{tel}")
    public ResponseEntity<?> lookTel(@PathVariable String tel){
        authService.lookTel(tel);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }



}
