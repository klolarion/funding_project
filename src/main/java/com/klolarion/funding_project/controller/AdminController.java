package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.dto.PaymentMethodDto;
import com.klolarion.funding_project.service.AdminServiceImpl;
import com.klolarion.funding_project.service.FundingServiceImpl;
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
@RequestMapping("/f1/admin")
@Slf4j
public class AdminController {
    private final AdminServiceImpl adminServiceImpl;
    private final ProductServiceImpl productServiceImpl;
    private final FundingServiceImpl fundingServiceImpl;
    private MemberServiceImpl memberServiceImpl;

    @GetMapping
    public String admin(Model model){
        model.addAttribute("products",productServiceImpl.allProducts() );
        System.out.println(model.getAttribute("foundMember"));
        return "admin";
    }

    @PostMapping("/product")
    public String addProduct(@RequestParam String productName, @RequestParam Long price, @RequestParam int stock) {
        adminServiceImpl.addProduct(productName, price, stock);
        log.debug("상품 추가 성공");
        return "redirect:/f1/admin";
    }

    //검색
    @GetMapping("/product")
    public String productSearch(@RequestParam Long productId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("foundProduct", productServiceImpl.getProduct(productId));
        return "redirect:/f1/admin";
    }

    @GetMapping("/funding")
    public String fundingSearch(@RequestParam Long fundingId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("foundFunding", adminServiceImpl.searchFunding(fundingId));
        return "redirect:/f1/admin";
    }

    @GetMapping("/member")
    public String memberSearch(@RequestParam Long memberId, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("foundMember", adminServiceImpl.searchMember(memberId));
        return "redirect:/f1/admin";
    }

    //상품 action
//    @PostMapping("/productDelete")
//    public String productDelete(@RequestParam Long productId) {
//
//        return "redirect:/admin";
//    }
    @PostMapping("/stock")
    public String addStock(@RequestParam Long productId, @RequestParam int stock) {
        adminServiceImpl.addStock(productId, stock);
        return "redirect:/f1/admin";
    }

    @PostMapping("/restock")
    public  String restock(@RequestParam Long productId) {
        adminServiceImpl.setRestock(productId);
        return "redirect:/f1/admin";
    }

    @PostMapping("/saleFinished")
    public String saleFinished(@RequestParam Long productId) {
        adminServiceImpl.setSellFinished(productId);
        return "redirect:/f1/admin";
    }

    @PostMapping("/paymentMethod")
    public String addPaymentMethod(@RequestParam int code, @RequestParam String paymentName, @RequestParam String accountNumber, @RequestParam Long availableAmount, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("paymentList", adminServiceImpl.addPaymentMethod(code, paymentName, accountNumber, availableAmount));
        return "redirect:/f1/admin";
    }

    @GetMapping("/paymentMethod")
    public String deletePaymentMethod(@RequestParam Long paymentMethodId) {
        adminServiceImpl.deletePaymentMethod(paymentMethodId);
        return "redirect:/f1/admin";
    }
}
