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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        qMemberStatus.memberStatusCode,
                        qMemberStatus.statusExpires

                )).from(qMember)
                .where(qMember.memberId.eq(memberId))
                .leftJoin(qMemberStatus).on(qMemberStatus.member.memberId.eq(memberId))
                .fetchOne();

    }

    public MyActivityDto getMyActivity(Long memberId){
        QFunding qFunding = QFunding.funding;
        QGroup qGroup = QGroup.group;
        QGroupStatus qGroupStatus = QGroupStatus.groupStatus;
        QPayment qPayment = QPayment.payment;


        return query.select(Projections.constructor(MyActivityDto.class,
                qFunding.countDistinct(),
                qPayment.amount.sum(),
                qGroup.countDistinct(),
                qGroupStatus.countDistinct()
                ))
                .from(qFunding)
                .join(qGroup).on(qGroup.groupLeader.memberId.eq(memberId).and(qGroup.groupActive.isTrue()))
                .join(qGroupStatus).on(qGroupStatus.groupMember.memberId.eq(memberId)
                        .and(qGroupStatus.accepted.isTrue()
                        .and(qGroupStatus.banned.isFalse())
                        .and(qGroupStatus.exited.isFalse())))
                .join(qPayment).on(qPayment.member.memberId.eq(memberId).and(qPayment.completed.isTrue()))
                .where(qFunding.member.memberId.eq(memberId))
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
                        .and(qPaymentMethodList.offCd.isFalse())
                        .and(qPaymentMethodList.mainPayment.isFalse())).fetch();
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
        PaymentMethodList fetched = query.selectFrom(qPaymentMethodList).where(
                qPaymentMethodList.member.memberId.eq(member.getMemberId())
                        .and(qPaymentMethodList.paymentMethod.paymentMethodId.eq(paymentMethodId))
        ).fetchFirst();


        //조회된 데이터의 off_cd를 확인 후 false로 변경
        if(fetched != null){
            long executed = query.update(qPaymentMethodList)
                    .set(qPaymentMethodList.offCd, false)
                    .where(qPaymentMethodList.paymentMethodListId.eq(fetched.getPaymentMethodListId())
                            .and(qPaymentMethodList.member.memberId.eq(member.getMemberId())))
                    .execute();
            return null;
        }else{
            //데이터가 없으면 새로 생성
            return paymentMethodListRepository.save(paymentMethodList);
        }
    }

    @Override
    public boolean makeMainPayment(Long paymentMethodListId) {
        Long memberId = currentMember.getMember().getMemberId();
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;

        try {
            query.update(qPaymentMethodList)
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
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
