package com.klolarion.funding_project.util;

import org.springframework.stereotype.Component;

@Component
public class ProgressCalculator {
    public double calculateProgress(Long currentFundingAmount, Long totalFundingAmount) {
        // 총 금액이 0인 경우를 처리하여 0으로 나누는 것을 방지
        if (totalFundingAmount == null || totalFundingAmount == 0) {
            return 0.0;
        }

        // 진행률 계산
        double progress = (double) currentFundingAmount / totalFundingAmount * 100;

        return progress;
    }
}
