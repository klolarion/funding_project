package com.klolarion.funding_project.service;

import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.member.MemberDto;
import com.klolarion.funding_project.dto.member.MyActivityDto;
import com.klolarion.funding_project.repository.MemberRepository;
import com.klolarion.funding_project.repository.PaymentMethodListRepository;
import com.klolarion.funding_project.service.blueprint.MemberService;
import com.klolarion.funding_project.util.CurrentMember;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final PaymentMethodListRepository paymentMethodListRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;
    private final JPAQueryFactory query;
    private final CurrentMember currentMember;


    public MemberDto getMemberPageData(Long memberId){
        QMember qMember = QMember.member;
        QMemberStatus qMemberStatus = QMemberStatus.memberStatus;

        return query.select(Projections.constructor(MemberDto.class,
                        qMember.memberId,
                        qMember.role.roleName,
                        qMember.email,
                        qMember.nickName,
                        qMember.provider,
                        qMemberStatus.memberStatusId,
                        qMemberStatus.memberStatusCode,
                        qMemberStatus.statusExpires

                )).from(qMember)
                .leftJoin(qMemberStatus).on(qMemberStatus.member.memberId.eq(memberId))
                .fetchOne();

    }

    public MyActivityDto getMyActivity(Long memberId){
        QFunding qFunding = QFunding.funding;
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QPayment qPayment = QPayment.payment;


        return query.select(Projections.constructor(MyActivityDto.class,
                qFunding.count(),
                qPayment.amount.sum(),
                qGroup.count(),
                qGroupStatus.count()
                ))
                .from(qFunding)
                .join(qGroup).on(qGroup.groupLeader.memberId.eq(memberId))
                .join(qGroupStatus).on(qGroupStatus.groupMember.memberId.eq(memberId))
                .join(qPayment).on(qPayment.member.memberId.eq(memberId))
                .where(qGroupStatus.accepted.isTrue()
                        .and(qGroupStatus.banned).isFalse()
                        .and(qGroupStatus.exited.isFalse())
                        .and(qGroup.groupActive.isTrue())
                        .and(qPayment.completed.isTrue()))
                .fetchOne();
    }

    @Override
    public Member setPink(Long memberId, String code) {
        return null;
    }

    @Override
    public Member setSilver(Long memberId, String code) {
        return null;
    }

//    public CustomUserDetails findMemberToCustom(String account) {
//        Optional<Member> findMember = memberRepository.findByAccount(account);
//        if (findMember.isPresent()) {
//            if(!findMember.get().isOffCd()) {
//                return findMember.get().memberToCustom();
//            }
//        }
//        return null;
//    }


    @Override
    public Member myInfo() {
        return currentMember.getMember();
    }

    @Override
    public Member getMember(String account) {
        return null;
    }

    @Override
    public List<Member>  searchMember(String memberName){
        QMember qMember = QMember.member;
        List<Member> fetch = query.selectFrom(qMember).where(qMember.nickName.contains(memberName)).fetch();
        em.flush();
        em.clear();
        return fetch;
    }


    @Override
    public List<PaymentMethodList> myPaymentLists(Long memberId) {
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;
        List<PaymentMethodList> paymentMethodList = query.selectFrom(qPaymentMethodList)
                .where(qPaymentMethodList.member.memberId.eq(memberId)
                        .and(qPaymentMethodList.offCd.isFalse())).fetch();
        em.flush();
        em.clear();
        return paymentMethodList;
    }

    // 결제수단 관련 수정시 캐시 데이터도 수정해야함

    @Override
    public PaymentMethodList addPaymentMethod(Long paymentMethodId) {
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;
        Member member = currentMember.getMember();
        PaymentMethod paymentMethod = query.selectFrom(qPaymentMethod).where(qPaymentMethod.paymentMethodId.eq(paymentMethodId)).fetchOne();

        PaymentMethodList paymentMethodList = new PaymentMethodList(paymentMethod, member);


        //이미 등록된 결제수단인지 확인
        //null이면 등록
        PaymentMethodList fetched = query.selectFrom(qPaymentMethodList).where(
                qPaymentMethodList.member.memberId.eq(member.getMemberId())
                        .and(qPaymentMethodList.paymentMethod.paymentMethodId.eq(paymentMethodId))
        ).fetchFirst();

        if(fetched != null){
            return null;
        }
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
    public PaymentMethodList getMainPaymentMethod(Long memberId){
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;

        PaymentMethodList paymentMethodList = query.selectFrom(qPaymentMethodList).where(
                qPaymentMethodList.member.memberId.eq(memberId)
                        .and(qPaymentMethodList.mainPayment.isTrue())
        ).fetchOne();

        em.flush();
        em.clear();
        return paymentMethodList;
    }

    @Override
    public boolean removePayment(Long paymentMethodListId) {
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;
        long result = query.update(qPaymentMethodList)
                .set(qPaymentMethodList.offCd, true)
                .where(qPaymentMethodList.paymentMethodListId.eq(paymentMethodListId)
                        .and(qPaymentMethodList.mainPayment.isFalse())) //주 결제수단이 아니어야 삭제가능
                .execute();
        em.flush();
        em.clear();
        return result == 1L;
    }


    /*캐시 삭제*/
    @Override
    public boolean logout() {
        return false;
    }

    /*캐시 삭제*/
    @Override
    public boolean leave() {
        QMember qMember = QMember.member;
        long result = query.update(qMember).set(qMember.offCd, true).where().execute();
        return result == 1;
    }





}
