package com.klolarion.funding_project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/paymentMethod")
public class PaymentMethodController {
//    private final PaymentServiceImpl paymentServiceImpl;

    @GetMapping
    public String payment(Model model, HttpSession session) {
//        Payment payment1 =
        session.getAttribute("member");
        
//        model.addAttribute("myPayment", paymentServiceImpl.getMyPayments() );
        return "paymentMethod";
    }
}
