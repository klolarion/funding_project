package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.service.AdminServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/f1/paymentMethod")
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
    public String addMyPaymentMethod(@RequestParam Long paymentMethodId, Model model, HttpSession session) {
        memberServiceImpl.addPaymentMethod(paymentMethodId);
        return "redirect:/f1/paymentMethod";
    }

    @PostMapping("/mainMethod")
    public String addMainPaymentMethod(@RequestParam Long myPaymentMethodId, Model model, HttpSession session) {
        memberServiceImpl.makeMainPayment(myPaymentMethodId);
        return "redirect:/f1/paymentMethod";
    }

    @PostMapping("/mainMethod/delete")
    public String deletePaymentMethod(@RequestParam Long myPaymentMethodId, Model model, HttpSession session) {
        memberServiceImpl.removePayment(myPaymentMethodId);
        return "redirect:/f1/paymentMethod";
    }
}
