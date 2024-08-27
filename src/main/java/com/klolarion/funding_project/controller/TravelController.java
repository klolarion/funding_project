package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.dto.group.GroupDto;
import com.klolarion.funding_project.dto.travel.TravelDto;
import com.klolarion.funding_project.service.GroupServiceImpl;
import com.klolarion.funding_project.service.TravelServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/travel")
@RequiredArgsConstructor
@Slf4j
public class TravelController {
    private final TravelServiceImpl travelService;
    private final GroupServiceImpl groupService;


    @GetMapping
    public String getMyTravelGroups(Model model){
        model.addAttribute("myLeaderTravelGroup", groupService.myLeaderTravelGroups());

        return "createTravel";
    }

    @PostMapping
    public String createTravel(@ModelAttribute TravelDto travelDto){
        travelService.createTravel(travelDto);
        return "redirect:/index";
    }
}
