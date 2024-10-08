package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.service.AdminServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/paymentMethod")
@Slf4j
public class PaymentMethodController {
//    private final PaymentServiceImpl paymentServiceImpl;
    private final AdminServiceImpl adminServiceImpl;
    private final MemberServiceImpl memberServiceImpl;

    @GetMapping
    public String paymentMethod(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        model.addAttribute("myPaymentList", memberServiceImpl.myPaymentLists(member.getMemberId()));
        model.addAttribute("paymentMethodList", adminServiceImpl.paymentMethodList());
        model.addAttribute("mainPaymentMethod", memberServiceImpl.getMainPaymentMethod());
        return "paymentMethod";
    }

    @PostMapping("/myPaymentMethod")
    public String addMyPaymentMethod(@RequestParam("paymentMethodId") Long paymentMethodId, Model model, HttpSession session) {
        memberServiceImpl.addPaymentMethod(paymentMethodId);
        return "redirect:/paymentMethod";
    }

    @PostMapping("/mainMethod")
    public String addMainPaymentMethod(@RequestParam("myPaymentMethodId") Long myPaymentMethodId, Model model, HttpSession session) {
        memberServiceImpl.makeMainPayment(myPaymentMethodId);
        return "redirect:/paymentMethod";
    }

    @PostMapping("/mainMethod/delete")
    public String deletePaymentMethod(@RequestParam("myPaymentMethodId") Long myPaymentMethodId, Model model, HttpSession session) {
        memberServiceImpl.removePayment(myPaymentMethodId);
        return "redirect:/paymentMethod";
    }
}
