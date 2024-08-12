package com.klolarion.funding_project.controller;

import org.springframework.http.ResponseEntity;

/**
 * 인증 관련 기능
 * 윤효정
 * */

public interface AuthController {
    ResponseEntity<?> login();
    ResponseEntity<?> register();
    ResponseEntity<?> findEmail();
    ResponseEntity<?> resetPassword();

}
