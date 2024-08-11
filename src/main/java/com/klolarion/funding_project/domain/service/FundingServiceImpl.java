package com.klolarion.funding_project.domain.service;

import com.klolarion.funding_project.domain.entity.Funding;
import com.klolarion.funding_project.domain.entity.PaymentMethod;

public class FundingServiceImpl implements FundingService{

    @Override
    public Funding createFunding(Funding funding) {
        return null;
    }

    @Override
    public boolean closeFunding(Funding funding) {
        funding.closeFunding();
        return funding.isClosed();
    }

    @Override
    public boolean deleteFunding(Funding funding) {
        funding.deleteFunding();
        return funding.isDeleted();
    }

    @Override
    public boolean completeFunding(Funding funding) {
        funding.completeFunding();
        return funding.isCompleted();
    }

    @Override
    public boolean joinFunding(Funding funding, PaymentMethod paymentMethod, Long amount) {
        boolean result = funding.addFundingAmount(amount);
        boolean deposit = paymentMethod.deposit(amount);
        return result && deposit;
    }
}
