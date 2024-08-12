package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.service.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MypageController {
    private final PaymentServiceImpl paymentServiceImpl;

    @GetMapping
    public String myPage(HttpSession session, Model model) {
        model.addAttribute("member", session.getAttribute("member") );
        model.addAttribute("myPaymentList", paymentServiceImpl.getMyPayments() );

        return "myPage";
    }

}
