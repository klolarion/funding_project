package com.klolarion.funding_project.domain.service;

import com.klolarion.funding_project.domain.entity.Funding;

public interface FundingService {

    Funding createFunding();
    boolean stopFunding();
    boolean removeFunding();
    boolean completeFunding();
}
