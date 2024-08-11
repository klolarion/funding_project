package com.klolarion.funding_project.application.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.domain.service.FundingServiceImpl;
import com.klolarion.funding_project.infrastructure.persistence.CustomFundingRepositoryImpl;
import com.klolarion.funding_project.infrastructure.persistence.InfraFundingRepository;
import com.klolarion.funding_project.interfaces.dto.JoinFundingDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationFundingService {
    private final InfraFundingRepository fundingRepository;
    private final CustomFundingRepositoryImpl customFundingRepository;
    private final FundingServiceImpl fundingService;
    private final InfraFundingRepository paymentRepository;


    public void startFunding(Member member, Product product, Group group, Long totalFundingAmount, String fundingAccount){
        Funding funding = new Funding(member, product, group, totalFundingAmount, fundingAccount);
        fundingRepository.save(funding);
    }

    public void closeFunding(Long fundingId) {
        Funding funding = fundingRepository.findById(fundingId).orElseThrow(() -> new UsernameNotFoundException("펀딩 정보 없음"));
        boolean result = fundingService.closeFunding(funding);
        if(result){
            fundingRepository.save(funding);
        }
    }

    public void completeFunding(Long fundingId) {
        Funding funding = fundingRepository.findById(fundingId).orElseThrow(() -> new UsernameNotFoundException("펀딩 정보 없음"));
        boolean result = fundingService.completeFunding(funding);
        if(result){
            fundingRepository.save(funding);
        }
    }

    public void deleteFunding(Long fundingId) {
        Funding funding = fundingRepository.findById(fundingId).orElseThrow(() -> new UsernameNotFoundException("펀딩 정보 없음"));
        boolean result = fundingService.deleteFunding(funding);
        if(result){
            fundingRepository.save(funding);
        }
    }

    @Transactional
    public void joinFunding(JoinFundingDto joinFundingDto){
        List<Object> list = customFundingRepository.joinFunding(joinFundingDto);
        Funding funding = (Funding) list.get(0);
        PaymentMethod paymentMethod = (PaymentMethod) list.get(1);

        fundingService.joinFunding(funding, paymentMethod, joinFundingDto.getAmount());

        fundingRepository.save(funding);
        paymentRepository.save(funding);

    }
}
