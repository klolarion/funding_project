package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.domain.entity.PaymentMethod;
import com.klolarion.funding_project.dto.JoinFundingDto;

import java.util.List;

public interface FundingService {

    /*전체 펀딩 조회*/
    List<Funding> allFundingList();

    /*내 그룹별 펀딩 리스트 조회*/
    List<Funding> allFundingListByGroup(List<Long> groupId);

    /*내 친구별 펀딩 리스트 조회*/
    List<Funding> allFundingListByMyFriend(List<Long> memberId);

    /*친구의 펀딩 리스트 조회*/
    List<Funding> fundingListByMyFriend(Long friendId);

    /*내 그룹의 펀딩 리스트 조회*/
    List<Funding> fundingListByMyGroup(Long groupId);

    /*상품별 펀딩 리스트 조회*/
    List<Funding> fundingListByProduct(Long productId);

    /*펀딩 시작*/
    Funding createFunding(Long memberId, Long productId, Long GroupId);

    /*펀딩 완료*/
    boolean completeFunding(Long fundingId);

    /*펀딩하기*/
    boolean joinFunding(JoinFundingDto joinFundingDto);
}


