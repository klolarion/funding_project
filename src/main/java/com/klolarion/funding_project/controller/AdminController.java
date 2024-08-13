package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.dto.PaymentMethodDto;
import com.klolarion.funding_project.service.AdminServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import com.klolarion.funding_project.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
//    public String productDelete(@RequestParam Long productId) {
//
//        return "redirect:/admin";
//    }
    @PostMapping("/updateStock")
    public String productDelete(@RequestParam Long productId, @RequestParam int stock) {
        adminServiceImpl.addStock(productId, stock);
        return "redirect:/admin";
    }

    @PostMapping("/restock")
    public  String restock(@RequestParam Long productId) {
        adminServiceImpl.setRestock(productId);
        return "redirect:/admin";
    }

    @PostMapping("/saleFinished")
    public String saleFinished(@RequestParam Long productId) {
        adminServiceImpl.setSellFinished(productId);
        return "redirect:/admin";
    }

    @PostMapping("/addPaymentMethod")
    public String addPaymentMethod(@RequestParam int code, @RequestParam String paymentName, @RequestParam String accountNumber, @RequestParam Long availableAmount, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("paymentList", adminServiceImpl.addPaymentMethod(code, paymentName, accountNumber, availableAmount));
        return "redirect:/admin";
    }

    @GetMapping("/paymentDelete")
    public String paymentDelete(@RequestParam Long paymentMethodId) {
        adminServiceImpl.deletePaymentMethod(paymentMethodId);
        return "redirect:/admin";
    }
}
