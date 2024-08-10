package com.klolarion.funding_project.presentation.controller;

import org.springframework.http.ResponseEntity;

/**
 * 펀딩 관련 기능
 * 윤효정
 * */

public interface FundingController {
    ResponseEntity<?> allFundingList();

    ResponseEntity<?> allFundingListByProduct();
    ResponseEntity<?> allFundingListByMyFriend();

    ResponseEntity<?> fundingListByMyFriend();
    ResponseEntity<?> fundingListByMyGroup();

    ResponseEntity<?> fundingListByProduct();

    ResponseEntity<?> startFunding();
    ResponseEntity<?> stopFunding();
    ResponseEntity<?> finishFunding();
    ResponseEntity<?> joinFunding();
}
