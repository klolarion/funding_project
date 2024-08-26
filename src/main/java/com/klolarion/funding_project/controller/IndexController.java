package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.Role;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/index")
@Slf4j
public class IndexController {
    private final FundingServiceImpl fundingServiceImpl;
    private final MemberServiceImpl memberService;
    private final GroupServiceImpl groupServiceImpl;


    @GetMapping
    public String getIndex(@AuthenticationPrincipal OAuth2User principal, Model model, HttpSession session) {
        // 로그인 여부와 상관없이 페이지를 렌더링
        String name = principal != null ? principal.getAttribute("name") : "Guest";

        model.addAttribute("name", name);
        model.addAttribute("allFundingList", fundingServiceImpl.allFundingList());

        if (name.equals("Guest")) {

            model.addAttribute("allGroupList", groupServiceImpl.allGroups());
        } else {
            Member member = memberService.myInfo();
            session.setAttribute("member", member);
            model.addAttribute("allGroupList", groupServiceImpl.allGroupExceptMy());
        }
        return "index";
    }

}
