package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Payment;
import com.klolarion.funding_project.service.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @GetMapping
    public String getMyPayments(Model model){
        model.addAttribute("myPayments", paymentService.getMyPayments());
        return "myPayments";
    }
}
