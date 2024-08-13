package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.JoinFundingDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.blueprint.FundingService;
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
@RequestMapping("/f1/detail")
public class DetailController {
    private final FundingServiceImpl fundingServiceImpl;

    @GetMapping
    public String detail(@RequestParam Long fundingId, Model model) {
        model.addAttribute("funding",fundingServiceImpl.fundingDetail(fundingId));
//        System.out.println(fundingId);
//        System.out.println(fundingServiceImpl.fundingDetail(fundingId));
        return "detail";
    }

    @PostMapping
    public String joinFunding(@RequestParam Long fundingId, @RequestParam Long amount, Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        JoinFundingDto joinFundingDto = new JoinFundingDto();
        joinFundingDto.setAmount(amount);
        joinFundingDto.setFundingId(fundingId);
        joinFundingDto.setMemberId(member.getMemberId());
        fundingServiceImpl.joinFunding(joinFundingDto);
        return "redirect:/f1";
    }
}
