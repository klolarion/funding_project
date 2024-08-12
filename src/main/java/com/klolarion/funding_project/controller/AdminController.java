package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.service.blueprint.AdminService;
import com.klolarion.funding_project.service.AdminServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final AdminServiceImpl adminServiceImpl;
    private MemberServiceImpl memberServiceImpl;

    @GetMapping
    public String admin(Model model){
        return "admin";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam String productName, @RequestParam Long price, @RequestParam int stock) {
        try {
            System.out.println("before sql");
            adminServiceImpl.addProduct(productName, price, stock);
            System.out.println("after sql");
        }catch (Exception e){
            System.out.println("error");
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

    @PostMapping("/memberSearch")
    public String foundMember(@RequestParam String keyword, RedirectAttributes redirectAttributes){
//        redirectAttributes.addFlashAttribute("foundMember", )
        return "redirect:/admin";
    }
}
