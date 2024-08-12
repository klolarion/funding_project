package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.dto.JoinFundingDto;
import org.springframework.http.ResponseEntity;

/**
 * 펀딩 관련 기능
 * 윤효정
 * */

public interface FundingController {
    ResponseEntity<?> allFundingList();

    ResponseEntity<?> allFundingListByGroup();
    ResponseEntity<?> allFundingListByMyFriend();

    ResponseEntity<?> fundingListByMyFriend();
    ResponseEntity<?> fundingListByMyGroup();

    ResponseEntity<?> fundingListByProduct();

    ResponseEntity<?> startFunding();

    ResponseEntity<?> joinFunding(JoinFundingDto joinFundingDto);
}
