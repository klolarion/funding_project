package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;

import java.util.List;

public interface MemberService {

    /*내 정보*/
    Member myInfo();

    /*내 결제수단 목록*/
    List<PaymentMethodList> myPaymentLists(Long memberId);

    /*결제수단 추가*/
    PaymentMethodList addPayment(Long paymentMethodId);

    /*주 결제수단으로 등록*/
    boolean makeMainPayment(Long paymentMethodListId);

    /*내 결제수단 삭제*/
    boolean removePayment(Long paymentMethodListId);

    /*로그아웃*/
    boolean logout();

    /*회원탈퇴*/
    boolean leave();
}
