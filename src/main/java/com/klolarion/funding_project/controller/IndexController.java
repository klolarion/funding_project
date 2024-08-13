package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.Role;
import com.klolarion.funding_project.service.FundingServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/")
public class IndexController {
    private final FundingServiceImpl fundingServiceImpl;
    private final MemberServiceImpl memberService;
//    Member member = new Member(new Role(1L,"user"), "0213@funding.java", "윤효정","1111");

    @GetMapping("/")
    public String goIndex(Model model, HttpSession session){
        Member member = memberService.myInfo();
        session.setAttribute("member", member);
//        System.out.println(member);
        model.addAttribute("AllFundingList", fundingServiceImpl.allFundingList());
        return "index";
    }

//    @GetMapping
//    public String index(Model model) {
////        model.addAttribute("allFundingList", fundingServiceImpl.allFundingList());
//        return "index";
//    }
}
