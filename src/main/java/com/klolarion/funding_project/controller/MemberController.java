package com.klolarion.funding_project.controller;

import org.springframework.http.ResponseEntity;

/**
 * 사용자 관련 기능
 * 윤효정
 * */

public interface MemberController {


    ResponseEntity<?> sendCodeToEmail();
    ResponseEntity<?> checkCode();

    ResponseEntity<?> changePassword();

    ResponseEntity<?> myPayments();
    ResponseEntity<?> addPayment();
    ResponseEntity<?> makeMainPayment();
    ResponseEntity<?> removePayment();

    ResponseEntity<?> logout();
    ResponseEntity<?> leave();
}
