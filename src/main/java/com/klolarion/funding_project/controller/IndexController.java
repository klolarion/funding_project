package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.Role;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
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
@RequestMapping("/f1")
public class IndexController {
    private final FundingServiceImpl fundingServiceImpl;
    private final MemberServiceImpl memberService;
    private final GroupServiceImpl groupServiceImpl;

    @GetMapping
    public String getIndex(Model model, HttpSession session){
        Member member = memberService.myInfo();
        session.setAttribute("member", member);
        model.addAttribute("allFundingList", fundingServiceImpl.allFundingList());
        return "index";
    }

}
