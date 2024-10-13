package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.payment.PaymentDto;
import com.klolarion.funding_project.service.blueprint.PaymentService;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final JPAQueryFactory query;
    private final EntityManager em;
    private final CurrentMember currentMember;
    @Override
    public List<PaymentDto> getMyPayments() {
        QMember qMember = QMember.member;
        QPayment qPayment = QPayment.payment;
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;
        Member member = currentMember.getMember();


        List<Tuple> tuples = query.select(qMember, qPayment, qPaymentMethod, qPaymentMethodList)
                .from(qMember)
                .join(qPayment).on(qPayment.member.memberId.eq(qMember.memberId))
                .join(qPaymentMethodList).on(qPaymentMethodList.member.memberId.eq(qMember.memberId))
                .join(qPaymentMethod).on(qPaymentMethod.paymentMethodId.eq(qPaymentMethodList.paymentMethod.paymentMethodId))
                .where(qMember.memberId.eq(member.getMemberId()))
                .fetch();

        List<PaymentDto> paymentDtos = new ArrayList<>();

        for(Tuple tuple : tuples){
            Payment payment = tuple.get(qPayment);
            PaymentMethodList fromAccount = tuple.get(qPaymentMethodList);

            PaymentDto tmp = new PaymentDto(
                    payment.getPaymentId(),
                    payment.getMember().getMemberId(),
                    payment.getPaymentAccount(),
                    fromAccount.getPaymentMethod().getPaymentName(),
                    payment.getTargetAccount(),
                    payment.getAmount(),
                    payment.getBalanceBefore(),
                    payment.getBalanceAfter(),
                    payment.getCreatedDate()
            );

            paymentDtos.add(tmp);

        }

        return paymentDtos;

    }

    @Override
    public List<PaymentMethod> paymentMethodList() {
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;
        List<PaymentMethod> fetched = query.selectFrom(qPaymentMethod).fetch();
        return fetched;
    }
}
