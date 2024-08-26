package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.service.FundingServiceImpl;
import com.klolarion.funding_project.service.GroupServiceImpl;
import com.klolarion.funding_project.service.ProductServiceImpl;
import com.klolarion.funding_project.service.TravelServiceImpl;
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
@RequestMapping("/funding")
//@RequestMapping("/f1/v1/funding")
@Slf4j
public class fundingController {
    private final FundingServiceImpl fundingServiceImpl;
    private final ProductServiceImpl productServiceImpl;
    private final GroupServiceImpl groupServiceImpl;
    private final TravelServiceImpl travelService;

    @GetMapping
    public String funding(Model model) {
        model.addAttribute("productList",productServiceImpl.allProducts());
        model.addAttribute("travelList", travelService.myTravels());
        model.addAttribute("myLeaderGroups", groupServiceImpl.myLeaderGroups());
        return "createFunding";
    }

    @PostMapping
    public String createFunding(@RequestParam(required = false, value = "productId") Long productId,
                                @RequestParam(required = false, value = "travelId") Long travelId,
                                @RequestParam(required = false, value = "groupId") Long groupId,
                                HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        fundingServiceImpl.createFunding(member.getMemberId(), productId, travelId, groupId);
        return "redirect:/index";
    }
}
