package com.klolarion.funding_project.domain.service;

import com.klolarion.funding_project.domain.entity.PaymentMethod;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;

import java.util.List;

public interface MemberService {
    boolean sendCodeToEmail();
    boolean checkCode();
    boolean changePassword();
    List<PaymentMethodList> myPayments();
    PaymentMethod addPayment();
    boolean makeMainPayment();
    boolean removePayment();
    boolean logout();
    boolean leave();
}
