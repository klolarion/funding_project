package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.domain.entity.PaymentMethod;
import com.klolarion.funding_project.dto.JoinFundingDto;

import java.util.List;

public interface FundingService {

    List<Funding> allFundingList();

    List<Funding> allFundingListByGroup(List<Long> groupId);

    List<Funding> allFundingListByMyFriend(List<Long> memberId);

    List<Funding> fundingListByMyFriend(Long friendId);

    List<Funding> fundingListByMyGroup(Long groupId);

    List<Funding> fundingListByProduct(Long productId);

    /*펀딩 시작*/
    Funding createFunding(Long memberId, Long productId, Long GroupId);

    /*펀딩 중단*/
    boolean closeFunding(Long fundingId);

    /*펀딩 삭제*/
    boolean deleteFunding(Long fundingId);

    /*펀딩 완료*/
    boolean completeFunding(Long fundingId);

    /*펀딩하기*/
    boolean joinFunding(JoinFundingDto joinFundingDto);
}


