package com.klolarion.funding_project.controller.api_v1;

import com.klolarion.funding_project.service.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/f2/v1/payment")
@RequiredArgsConstructor
public class PaymentControllerV1 {
    private final PaymentServiceImpl paymentService;


    @GetMapping
    public ResponseEntity<?> getPayments(){
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getMyPayments());
    }
}
