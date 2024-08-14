package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import com.klolarion.funding_project.service.ProductServiceImpl;
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
@RequestMapping("/f1/v2/funding")
@Slf4j
public class CreateFundingControllerV2 {
    private final FundingServiceImpl fundingServiceImpl;
    private final ProductServiceImpl productServiceImpl;
    private final MemberServiceImpl memberServiceImpl;

    @GetMapping
    public String funding(Model model) {
        model.addAttribute("productList",productServiceImpl.allProducts());

        return "createFunding";
    }

    @PostMapping
    public String createFunding(@RequestParam Long productId, @RequestParam(required = false) Long groupId, HttpSession session) {
        Member member = (Member) session.getAttribute("member");

        fundingServiceImpl.createFunding(member.getMemberId(), productId, groupId);
        return "redirect:/f1";
    }
}
