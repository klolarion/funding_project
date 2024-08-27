package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.Payment;
import com.klolarion.funding_project.domain.entity.QPayment;
import com.klolarion.funding_project.service.blueprint.PaymentService;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public List<Payment> getMyPayments() {
        QPayment qPayment = QPayment.payment;
        Member member = currentMember.getMember();

        return query.selectFrom(qPayment).where(qPayment.member.memberId.eq(member.getMemberId())).fetch();

    }
}
