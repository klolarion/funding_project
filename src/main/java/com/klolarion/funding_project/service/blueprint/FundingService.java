package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.dto.funding.JoinFundingDto;

import java.util.List;
import java.util.Map;

public interface FundingService {

    /*전체 펀딩 조회*/
    List<FundingListDto> allFundingList();

    /*내 펀딩 리스트 조회*/
    List<FundingListDto> myFundingList(Long memberId);

    /*펀딩 조회*/
    FundingListDto fundingDetail(Long fundingID);

    /*내 그룹별 펀딩 리스트 조회*/
    Map<String, List<FundingListDto>> allFundingListByGroup(Long groupId);

    /*내 친구별 펀딩 리스트 조회*/
    Map<String, List<FundingListDto>> allFundingListByMyFriend(Long memberId);

    /*친구의 펀딩 리스트 조회*/
    List<FundingListDto> fundingListByMyFriend(Long friendId);

    /*내 그룹의 펀딩 리스트 조회*/
    List<FundingListDto> fundingListByMyGroup(Long groupId);

    /*상품별 펀딩 리스트 조회*/
    List<FundingListDto> fundingListByProduct(Long productId);

    /*펀딩 시작*/
    Funding createFunding(Long memberId, Long productId, Long travelId, Long GroupId, int statusCode);

    Funding createFundingApi(Long productId, Long groupId, Long travelId);

    /*펀딩 완료*/
    boolean completeFunding(Long fundingId);

    /*펀딩하기*/
    boolean joinFunding(JoinFundingDto joinFundingDto);

    /*펀딩 검색*/
    List<FundingListDto> searchFunding(String searchParam, Integer fundingCategoryCode);
}


