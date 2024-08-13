package com.klolarion.funding_project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/f1")
public class PaymentMethodController {
//    private final PaymentServiceImpl paymentServiceImpl;

    @GetMapping("/paymentMethodGet")
    public String paymentMethod(Model model, HttpSession session) {
        session.getAttribute("member");
        
//        model.addAttribute("myPayment", paymentServiceImpl.getMyPayments() );
        return "paymentMethod";
    }
}
