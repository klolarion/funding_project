package com.klolarion.funding_project.infrastructure.persistence;

import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import com.klolarion.funding_project.application.port.out.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
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
    public List<PaymentMethodList> myPaymentMethods() {
        return null;
    }

    @Override
    public PaymentMethodList addPaymentMethod() {
        return null;
    }

    @Override
    public ResponseEntity<?> makeMainPaymentMethod() {
        return null;
    }

    @Override
    public boolean removePaymentMethod() {
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
