package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.repository.MemberRepository;
import com.klolarion.funding_project.repository.PaymentMethodListRepository;
import com.klolarion.funding_project.service.blueprint.MemberService;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
    private final PaymentMethodListRepository paymentMethodListRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;
    private final JPAQueryFactory query;
    private final CurrentMember currentMember;

    public CustomUserDetails findMemberToCustom(String account) {
        Optional<Member> findMember = memberRepository.findByAccount(account);
        if (findMember.isPresent()) {
            if(!findMember.get().isOffCd()) {
                return findMember.get().memberToCustom();
            }
        }
        return null;
    }

    @Override
    public Member myInfo() {
        return currentMember.getMember();
    }

    @Override
    public List<PaymentMethodList> myPaymentLists(Long memberId) {
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;
        List<PaymentMethodList> paymentMethodList = query.selectFrom(qPaymentMethodList).where(qPaymentMethodList.member.memberId.eq(memberId)).fetch();
        em.flush();
        em.clear();
        return paymentMethodList;
    }

    @Override
    public PaymentMethodList addPayment(Long paymentMethodId) {
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;
        Member member = currentMember.getMember();
        PaymentMethod paymentMethod = query.selectFrom(qPaymentMethod).where(qPaymentMethod.paymentMethodId.eq(paymentMethodId)).fetchOne();

        PaymentMethodList paymentMethodList = new PaymentMethodList(paymentMethod, member);
        return paymentMethodListRepository.save(paymentMethodList);

    }


    @Override
    public boolean makeMainPayment(Long paymentMethodListId) {
        Long memberId = currentMember.getMember().getMemberId();
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;
        long result = query.update(qPaymentMethodList)
                .set(qPaymentMethodList.mainPayment,
                        new CaseBuilder()
                                .when(qPaymentMethodList.paymentMethodListId.eq(paymentMethodListId)
                                        .and(qPaymentMethodList.member.memberId.eq(memberId))).then(true)
                                .otherwise(false)
                )
                .where(qPaymentMethodList.member.memberId.eq(memberId))
                .execute();
        em.flush();
        em.clear();
        return result == 1L;
    }

    @Override
    public boolean removePayment(Long paymentMethodListId) {
        Long memberId = currentMember.getMember().getMemberId();
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;
        long result = query.update(qPaymentMethodList)
                .set(qPaymentMethodList.offCd, true)
                .where(qPaymentMethodList.member.memberId.eq(memberId))
                .execute();
        em.flush();
        em.clear();
        return result == 1L;
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
