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
@RequestMapping("/group")
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
    public String createGroup(@RequestParam("groupName") String groupName, @RequestParam("groupCategory") String groupCategory) {
        groupServiceImpl.startGroup(groupName, groupCategory);
        return "redirect:/group";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("groupId") Long groupId, Model model) {
        model.addAttribute("groupDetail", groupServiceImpl.groupDetail(groupId));
        model.addAttribute("groupMembers", groupServiceImpl.groupMembers(groupId));
        model.addAttribute("requestMembers", groupServiceImpl.requestedMembersToMyGroup(groupId));
        model.addAttribute("invitedMembers", groupServiceImpl.invitedMembersToMyGroup(groupId));
        return "groupDetail";
    }

    @PostMapping("/detail/member")
    public String findMember(@RequestParam("memberName") String memberName, @RequestParam("groupId") Long groupId, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("foundMembers", memberServiceImpl.searchMember(memberName));

        return "redirect:/group/detail?groupId=" + groupId;
    }

    @PostMapping("/detail/invite")
    public String inviteMember(@RequestParam("memberId") Long memberId, @RequestParam("groupId") Long groupId, RedirectAttributes redirectAttributes){
        groupServiceImpl.inviteMember( groupId, memberId);
        return "redirect:/group/detail?groupId=" + groupId;
    }

    @PostMapping("/detail/request")
    public String acceptRequest(@RequestParam("memberId") Long memberId, @RequestParam("groupId") Long groupId, RedirectAttributes redirectAttributes){
        groupServiceImpl.acceptMemberRequest( groupId, memberId);
        return "redirect:/group/detail?groupId=" + groupId;
    }

    //group Info
    @GetMapping("/info")
    public String info(@RequestParam("groupId") Long groupId, Model model) {
        try {
            model.addAttribute("groupDetail", groupServiceImpl.groupInfoDetail(groupId));
            model.addAttribute("groupMembers", groupServiceImpl.groupMembers(groupId));
            Map<String, List<FundingListDto>> groupedMap = fundingServiceImpl.allFundingListByGroup(groupId);
            List<FundingListDto> singleList = groupedMap.values()
                    .stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            model.addAttribute("fundingList", singleList);

            return "groupInfo";
        }catch (Exception e){
            System.out.println("info error, " + e);
        }
        return null;
    }

    @PostMapping("/info/request")
    public String requestGroup(@RequestParam("groupId") Long groupId){
        groupServiceImpl.requestToGroup(groupId);
        return "redirect:/group/info?groupId=" + groupId;
    }

}
