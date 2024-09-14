package com.klolarion.funding_project.dto.member;

import com.klolarion.funding_project.domain.entity.Payment;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import com.klolarion.funding_project.dto.funding.FundingListDto;
import com.klolarion.funding_project.dto.group.GroupDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPageDto {
    private List<FundingListDto> fundingListDtos;
    private PaymentMethodList mainPaymentMethod;
    private MemberDto memberDto;
    private MyActivityDto myActivity;

}
