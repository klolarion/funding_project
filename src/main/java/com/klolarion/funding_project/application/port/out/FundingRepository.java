package com.klolarion.funding_project.application.port.out;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.interfaces.dto.JoinFundingDto;

import java.util.List;

public interface FundingRepository {

    /*전체 펀딩 목록*/
    List<Funding> allFundingList();

    /*내 그룹 전체의 펀딩 목록*/
    List<Funding> allFundingListByGroup(List<Long> groupId);

    /*내 친구 전체의 펀딩 목록*/
    List<Funding> allFundingListByMyFriend(List<Long> memberId);

    /*특정 친구의 펀딩 목록*/
    List<Funding> fundingListByMyFriend(Long friendId);

    /*특정 그룹의 펀딩 목록*/
    List<Funding> fundingListByMyGroup(Long groupId);

    /*특정 상품의 펀딩 목록*/
    List<Funding> fundingListByProduct(Long productId);


    /*펀딩하기(입금)*/
    List<Object> joinFunding(JoinFundingDto joinFundingDto);

}
