package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.Role;
import com.klolarion.funding_project.service.FundingService;
import com.klolarion.funding_project.service.FundingServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/")
public class IndexController {
    private final FundingServiceImpl fundingServiceImpl;
    Member member = new Member(new Role(1L,"user"), "0213@funding.java", "윤효정","1111");

    @GetMapping("/")
    public String goIndex(Model model, HttpSession session){
        session.setAttribute("member", member);
        System.out.println(member);
        model.addAttribute("AllFundingList", fundingServiceImpl.allFundingList());
        return "index";
    }

//    @GetMapping
//    public String index(Model model) {
////        model.addAttribute("allFundingList", fundingServiceImpl.allFundingList());
//        return "index";
//    }
}
