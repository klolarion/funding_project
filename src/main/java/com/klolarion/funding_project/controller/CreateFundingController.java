package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.service.FundingService;
import com.klolarion.funding_project.service.FundingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/create")
@Slf4j
public class CreateFundingController {
    private final FundingServiceImpl fundingServiceImpl;

    @GetMapping
    public String createFunding(Model model) {
//        model.addAttribute("funding", fundingServiceImpl.createFunding(1L, null,null) );
        return "createFunding";
    }

//    @PostMapping
//    public String createFunding(@RequestParam String productId, @RequestParam String groupId) {
//
////        log.debug(productId + groupId);
////        return "redirect:/createFunding";
//
//    }
}
