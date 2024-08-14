package com.klolarion.funding_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/f1/register")
public class RegisterController {
    @GetMapping
    public String register() {
        return "register";
    }

//    @PostMapping
//    public String registerMember(@RequestParam ) {
//    }

}
