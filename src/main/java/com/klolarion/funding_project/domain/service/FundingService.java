package com.klolarion.funding_project.domain.service;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.domain.entity.PaymentMethod;

public interface FundingService {

    /*펀딩 시작*/
    Funding createFunding(Funding funding);

    /*펀딩 중단*/
    boolean closeFunding(Funding funding);

    /*펀딩 삭제*/
    boolean deleteFunding(Funding funding);

    /*펀딩 완료*/
    boolean completeFunding(Funding funding);

    /*펀딩하기*/
    boolean joinFunding(Funding funding, PaymentMethod paymentMethod, Long amount);
}


