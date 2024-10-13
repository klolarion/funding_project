package com.klolarion.funding_project.dto.member;

import com.klolarion.funding_project.dto.payment.PaymentMethodListDto;
import com.klolarion.funding_project.dto.funding.FundingListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPageDto {
    private List<FundingListDto> fundingListDtos;
    private PaymentMethodListDto mainPaymentMethodDto;
    private MemberDto memberDto;
    private MyActivityDto myActivity;

}
