package com.klolarion.funding_project.interfaces.controller;

import com.klolarion.funding_project.application.service.ApplicationFundingService;
import com.klolarion.funding_project.interfaces.dto.JoinFundingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class FundingControllerImpl implements FundingController{
    private final ApplicationFundingService fundingService;


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

    @Override
    public ResponseEntity<?> stopFunding() {
        return null;
    }

    //흐름확인용 구현
    @Override
    @PutMapping("/funding/{fundingId}")
    public ResponseEntity<?> closeFunding(@PathVariable Long fundingId) {
        fundingService.closeFunding(fundingId);
        return null;
    }

    //흐름확인용 구현
    @Override
    @PostMapping("/funding")
    public ResponseEntity<?> joinFunding(@RequestBody JoinFundingDto joinFundingDto) {
        fundingService.joinFunding(joinFundingDto);
        return null;
    }
}
