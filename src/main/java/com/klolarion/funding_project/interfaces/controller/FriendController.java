package com.klolarion.funding_project.interfaces.controller;

import org.springframework.http.ResponseEntity;

/**
 * 친구 관련 기능
 * 윤효정
 * */
public interface FriendController {

    ResponseEntity<?> myFriends();
    ResponseEntity<?> addFriend();
    ResponseEntity<?> acceptRequest();
    ResponseEntity<?> denyRequest();
    ResponseEntity<?> removeFriend();
    ResponseEntity<?> banFriend();
}
