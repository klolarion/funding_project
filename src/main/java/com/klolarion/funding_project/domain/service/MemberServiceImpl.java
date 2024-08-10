package com.klolarion.funding_project.domain.service;

import com.klolarion.funding_project.domain.entity.PaymentMethod;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;

import java.util.List;

public class MemberServiceImpl implements MemberService{
    @Override
    public boolean sendCodeToEmail() {
        return false;
    }

    @Override
    public boolean checkCode() {
        return false;
    }

    @Override
    public boolean changePassword() {
        return false;
    }

    @Override
    public List<PaymentMethodList> myPayments() {
        return null;
    }

    @Override
    public PaymentMethod addPayment() {
        return null;
    }

    @Override
    public boolean makeMainPayment() {
        return false;
    }

    @Override
    public boolean removePayment() {
        return false;
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public boolean leave() {
        return false;
    }
}
