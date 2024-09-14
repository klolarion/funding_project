package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.domain.entity.FriendStatus;
import com.klolarion.funding_project.dto.friend.FriendRequestDto;
import com.klolarion.funding_project.service.FriendServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friend")
@Slf4j
public class FriendController {
    private final FriendServiceImpl friendService;

    @GetMapping
    public String friend(Model model) {
        model.addAttribute("myFriends", friendService.friendList());
        model.addAttribute("myRequests", friendService.requestList());
        return "friend";
    }

    @GetMapping("/{friendName}")
    public String searchFriend(@PathVariable String friendName, Model model) {

        model.addAttribute("searchedFriends", friendService.searchFriend(friendName));

        return "friend";
    }


    @GetMapping("/requests")
    public String getFriendRequests(Model model) {

        List<FriendRequestDto> friendRequestDtos = friendService.requestList();

        model.addAttribute("requests", friendRequestDtos);
        return "friend";

    }

    @PostMapping("/{friendId}")
    public String addFriend(Model model, @PathVariable Long friendId) {
        FriendStatus friendStatus = friendService.addFriend(friendId);
        return "friend";
    }


    @PostMapping("/accept/{friendStatusId}")
    public String acceptFriend(@PathVariable Long friendStatusId) {

        friendService.acceptFriendRequest(friendStatusId);
        return "friend";

    }
}
