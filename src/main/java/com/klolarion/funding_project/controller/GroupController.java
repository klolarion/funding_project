package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.service.GroupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/f1/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupServiceImpl groupServiceImpl;

    @GetMapping
    public String group(Model model ) {
        model.addAttribute("myGroup", groupServiceImpl.myGroups());
//        System.out.println(groupServiceImpl.myGroups());
        return "group";
    }

    @PostMapping
    public String createGroup(@RequestParam String groupName) {
        groupServiceImpl.startGroup(groupName);
        return "redirect:/f1/group";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam Long groupId, Model model) {
//        System.out.println(groupId);
        model.addAttribute("groupDetail", groupServiceImpl.groupDetail(groupId));
        return "groupDetail";
    }
}
