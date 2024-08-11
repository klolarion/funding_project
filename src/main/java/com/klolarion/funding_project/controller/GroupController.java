package com.klolarion.funding_project.controller;

import org.springframework.http.ResponseEntity;

/**
 * 그룹 관련 기능
 * 윤효정
 * */

public interface GroupController {
    ResponseEntity<?> myGroups();
    ResponseEntity<?> groupDetail();
    ResponseEntity<?> startGroup();
    ResponseEntity<?> inviteMember();
    ResponseEntity<?> acceptInviteRequest();
    ResponseEntity<?> requestToGroup();
    ResponseEntity<?> acceptMemberRequest();
    ResponseEntity<?> exileMember();
    ResponseEntity<?> exitGroup();
    ResponseEntity<?> banMember();


}
