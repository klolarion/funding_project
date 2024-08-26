package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import com.klolarion.funding_project.service.PaymentServiceImpl;
import com.klolarion.funding_project.util.CurrentMember;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
@Slf4j
public class MypageController {
    private final PaymentServiceImpl paymentServiceImpl;
    private final FundingServiceImpl fundingServiceImpl;
    private final GroupServiceImpl groupServiceImpl;
    private final MemberServiceImpl memberServiceImpl;
    private final CurrentMember currentMember;

    @GetMapping
    public String myPage(HttpSession session, Model model) {
        //조회가 5개!!
        model.addAttribute("member", session.getAttribute("member"));
        model.addAttribute("myFundingList", fundingServiceImpl.myFundingList());
        model.addAttribute("myLeaderGroup", groupServiceImpl.myLeaderGroups());
        model.addAttribute("myMainPayment", memberServiceImpl.getMainPaymentMethod());
        model.addAttribute("myGroup", groupServiceImpl.myGroups());
        model.addAttribute("myPayments", paymentServiceImpl.getMyPayments());

        return "myPage";
    }



}
