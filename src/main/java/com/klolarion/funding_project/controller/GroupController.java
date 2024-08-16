package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/f1/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupServiceImpl groupServiceImpl;
    private final MemberServiceImpl memberServiceImpl;
    private final FundingServiceImpl fundingServiceImpl;

    @GetMapping
    public String group(Model model ) {
        model.addAttribute("myLeaderGroup", groupServiceImpl.myLeaderGroups());
//        System.out.println(groupServiceImpl.myLeaderGroups());
        return "group";
    }

    @GetMapping("/check")
    public boolean groupNameCheck(String groupName){
        return groupServiceImpl.groupNameCheck(groupName);
    }

    @PostMapping
    public String createGroup(@RequestParam String groupName, String groupCategory) {
        groupServiceImpl.startGroup(groupName, groupCategory);
        return "redirect:/f1/group";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam Long groupId, Model model) {
//        System.out.println(groupId);
        model.addAttribute("groupDetail", groupServiceImpl.groupDetail(groupId));
        model.addAttribute("groupMembers", groupServiceImpl.groupMembers(groupId));
        model.addAttribute("requestMembers", groupServiceImpl.requestedMembersToMyGroup(groupId));
        model.addAttribute("invitedMembers", groupServiceImpl.invitedMembersToMyGroup(groupId));
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

    @PostMapping("/detail/request")
    public String acceptRequest(@RequestParam Long memberId, @RequestParam Long groupId, RedirectAttributes redirectAttributes){
        groupServiceImpl.acceptMemberRequest( groupId, memberId);
        return "redirect:/f1/group/detail?groupId=" + groupId;
    }

    //group Info
    @GetMapping("/info")
    public String info(@RequestParam Long groupId, Model model) {
        model.addAttribute("groupDetail", groupServiceImpl.groupDetail(groupId));
        model.addAttribute("groupMembers", groupServiceImpl.groupMembers(groupId));
        Map<String, List<FundingListDto>> groupedMap = fundingServiceImpl.allFundingListByGroup(groupId);
        List<FundingListDto> singleList = groupedMap.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
//        System.out.println(singleList);
        model.addAttribute("fundingList", singleList);

        return "groupInfo";
    }

    @PostMapping("/info/request")
    public String requestGroup(@RequestParam Long groupId){
        groupServiceImpl.requestToGroup(groupId);
        return "redirect:/f1/group/info?groupId=" + groupId;
    }

}
