package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final AdminServiceImpl adminServiceImpl;
    private final ProductServiceImpl productServiceImpl;
    private MemberServiceImpl memberServiceImpl;

    @GetMapping
    public String admin(Model model){
        model.addAttribute("products",productServiceImpl.allProducts() );
        return "admin";
    }

    //상품추가
    @PostMapping("/add")
    public String addProduct(@RequestParam String productName, @RequestParam Long price, @RequestParam int stock) {
        adminServiceImpl.addProduct(productName, price, stock);
        log.debug("상품 추가 성공");
        return "redirect:/admin";
    }

    //검색
    @PostMapping("/productSearch")
    public String productSearch(@RequestParam Long productId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("foundProduct", productServiceImpl.getProduct(productId));

        return "redirect:/admin";
    }

    @PostMapping("/fundingSearch")
    public String fundingSearch(@RequestParam Long fundingId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("foundFunding", productServiceImpl.getProduct(fundingId));
        return "redirect:/admin";
    }

    @PostMapping("/memberSearch")
    public String memberSearch(@RequestParam Long memberId, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("foundMember", adminServiceImpl.searchMember(memberId));
        return "redirect:/admin";
    }

    //상품 action
//    @PostMapping("/productDelete")
//    public String productDelete(@RequestParam Long productId, RedirectAttributes redirectAttributes) {
//
//        return "redirect:/admin";
//    }
    @PostMapping("/updateStock")
    public String productDelete(@RequestParam Long productId, @RequestParam int stock, RedirectAttributes redirectAttributes) {
        adminServiceImpl.addStock(productId, stock);
        return "redirect:/admin";
    }
}
