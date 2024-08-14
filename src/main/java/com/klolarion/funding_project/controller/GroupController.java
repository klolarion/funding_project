package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.service.GroupServiceImpl;
import com.klolarion.funding_project.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/f1/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupServiceImpl groupServiceImpl;
    private final MemberServiceImpl memberServiceImpl;

    @GetMapping
    public String group(Model model ) {
        model.addAttribute("myLeaderGroup", groupServiceImpl.myLeaderGroups());
//        System.out.println(groupServiceImpl.myLeaderGroups());
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
//        model.addAttribute("myGroup", memberServiceImpl.find());
        return "groupDetail";
    }

    @PostMapping("/detail/member")
    public String findMember(@RequestParam String memberName, @RequestParam Long groupId, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("foundMembers", memberServiceImpl.searchMember(memberName));

        return "redirect:/f1/group/detail?groupId=" + groupId;
    }

    @PostMapping("/detail/invite")
    public String inviteMember(@RequestParam Long memberId, @RequestParam Long groupId, RedirectAttributes redirectAttributes){
        groupServiceImpl.inviteMember( groupId, memberId);
        return "redirect:/f1/group/detail?groupId=" + groupId;
    }

}
