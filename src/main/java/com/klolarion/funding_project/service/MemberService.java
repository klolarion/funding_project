package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.PaymentMethod;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;

import java.util.List;

public interface MemberService {

    Member myInfo();
    List<PaymentMethodList> myPayments(Long memberId);
    PaymentMethod addPayment(Long paymentMethodId);
    boolean makeMainPayment(Long paymentMethodListId);
    boolean removePayment(Long paymentMethodListId);
    boolean logout();
    boolean leave();
}
