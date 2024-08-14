package com.klolarion.funding_project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.repository.MemberRepository;
import com.klolarion.funding_project.repository.PaymentMethodListRepository;
import com.klolarion.funding_project.service.blueprint.MemberService;
import com.klolarion.funding_project.util.CurrentMember;
import com.klolarion.funding_project.util.RedisService;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
    private final PaymentMethodListRepository paymentMethodListRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;
    private final JPAQueryFactory query;
    private final CurrentMember currentMember;
    private final RedisService redisService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Member getMemberCache() throws JsonProcessingException {
//        memberRepository.findByAccount("account", LockModeType.PESSIMISTIC_READ);
        Member memberCache = null;
        String mString = redisService.getData("account" + "_funding_cache");
        if (mString != null) {
            //redis에 저장된 데이터를 entity로 변환
            memberCache = objectMapper.readValue(mString, Member.class);
            return memberCache;
        }else{
            //캐시에 데이터가 없으면 DB에서 조회
            memberCache = memberRepository.findByAccount("account").orElseThrow(()->new UsernameNotFoundException("사용자 조회 실패"));
            System.out.println("DB data : " + memberCache.getMemberName());
            return memberCache;
        }
    }

    @Override
    public void setMemberCache(Member member){
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String value = objectMapper.writeValueAsString(member);

            redisService.setData(member.getAccount() + "_funding_cache", value, 10, TimeUnit.MINUTES);

            System.out.println(">> Data input success <<");

        } catch (JsonProcessingException e) {
            System.out.println(">> Data input failed <<");
            throw new RuntimeException(e);
        }
    }

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
        List<PaymentMethodList> paymentMethodList = query.selectFrom(qPaymentMethodList)
                .where(qPaymentMethodList.member.memberId.eq(memberId)
                        .and(qPaymentMethodList.offCd.isFalse())).fetch();
        em.flush();
        em.clear();
        return paymentMethodList;
    }

    @Override
    public PaymentMethodList addPaymentMethod(Long paymentMethodId) {
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
    public PaymentMethodList getMainPaymentMethod(){
        Member member = currentMember.getMember();
        QPaymentMethodList qPaymentMethodList = QPaymentMethodList.paymentMethodList;

        PaymentMethodList paymentMethodList = query.selectFrom(qPaymentMethodList).where(
                qPaymentMethodList.member.memberId.eq(member.getMemberId())
                        .and(qPaymentMethodList.mainPayment.isTrue())
        ).fetchOne();

        em.flush();
        em.clear();
        return paymentMethodList;
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
