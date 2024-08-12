package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.PaymentMethod;
import com.klolarion.funding_project.domain.entity.QPaymentMethod;
import com.klolarion.funding_project.repository.PaymentMethodRepository;
import com.klolarion.funding_project.service.blueprint.PaymentMethodService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final JPAQueryFactory query;
    private final EntityManager em;
    @Override
    public PaymentMethod addPaymentMethod(int code, String paymentName, String accountNumber, Long availableAmount) {
        PaymentMethod paymentMethod = new PaymentMethod(code, paymentName, accountNumber, availableAmount);
        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        return saved;
    }

    @Override
    public boolean deletePaymentMethod(Long paymentMethodId) {
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;
        long result = query.delete(qPaymentMethod).where(qPaymentMethod.paymentMethodId.eq(paymentMethodId)).execute();
        em.flush();
        em.clear();
        return result == 1L;
    }
}
