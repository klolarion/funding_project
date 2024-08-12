package com.klolarion.funding_project.controller;

import com.klolarion.funding_project.dto.JoinFundingDto;
import com.klolarion.funding_project.service.FundingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class FundingControllerImpl implements FundingController{
    private final FundingServiceImpl fundingService;


    @Override
    public ResponseEntity<?> allFundingList() {
        return null;
    }

    @Override
    public ResponseEntity<?> allFundingListByGroup() {
        return null;
    }

    @Override
    public ResponseEntity<?> allFundingListByMyFriend() {
        return null;
    }

    @Override
    public ResponseEntity<?> fundingListByMyFriend() {
        return null;
    }

    @Override
    public ResponseEntity<?> fundingListByMyGroup() {
        return null;
    }

    @Override
    public ResponseEntity<?> fundingListByProduct() {
        return null;
    }

    @Override
    public ResponseEntity<?> startFunding() {
        return null;
    }


    //흐름확인용 구현
    @Override
    @PostMapping("/funding")
    public ResponseEntity<?> joinFunding(@RequestBody JoinFundingDto joinFundingDto) {
        boolean result = fundingService.joinFunding(joinFundingDto);
        return null;
    }
}
