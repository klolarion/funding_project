package com.klolarion.funding_project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.klolarion.funding_project.domain.entity.*;
import com.klolarion.funding_project.dto.auth.RegisterDto;
import com.klolarion.funding_project.repository.MemberRepository;
import com.klolarion.funding_project.repository.PaymentMethodListRepository;
import com.klolarion.funding_project.repository.RoleRepository;
import com.klolarion.funding_project.service.blueprint.MemberService;
import com.klolarion.funding_project.util.CurrentMember;
import com.klolarion.funding_project.util.RedisService;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    private final RedisService redisService;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Member getMember(String account){
        return memberRepository.findByAccount(account).orElseThrow(() -> new UsernameNotFoundException("사용자 조회 실패"));
    }


    @Override
    public boolean save(RegisterDto registerDto) {
        Optional<Role> role = roleRepository.findById(2L); //USER
        Role defauleRole = role.orElseThrow(() -> new UsernameNotFoundException("Role not found"));
        Member member = new Member(
                registerDto.getAccount(),
                registerDto.getName(),
                registerDto.getEmail(),
                defauleRole
        );
        try {
            memberRepository.save(member);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Member getMemberCache() throws JsonProcessingException {
//        memberRepository.findByAccount("account", LockModeType.PESSIMISTIC_READ);
        Member memberCache = null;
        Member mString = (Member) redisService.getData("account" + "_funding_cache");
        if (mString != null) {
            //redis에 저장된 데이터를 entity로 변환
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
    public List<Member>  searchMember(String memberName){
        QMember qMember = QMember.member;
        List<Member> fetch = query.selectFrom(qMember).where(qMember.memberName.contains(memberName)).fetch();
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
        return false;
    }



    //구글로그인 테스트용
    @Override
    public Member saveOrUpdateUserGoogle(OAuth2User oAuth2User) {

        Optional<Role> role = roleRepository.findById(2L); //USER
        Role defauleRole = role.orElseThrow(() -> new UsernameNotFoundException("Role not found"));

        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        boolean enabled = oAuth2User.getAttribute("email_verified");

        return memberRepository.findByAccount(googleId)
                .map(member -> {
                    member.setEmail(email);
                    member.setMemberName(name);
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    Member member = new Member();
                    member.setAccount(googleId);
                    member.setEmail(email);
                    member.setMemberName(name);
                    member.setRole(defauleRole); // 기본 권한 설정
                    member.setProvider("Google");
                    member.setEnabled(enabled);
                    return memberRepository.save(member);
                });
    }

    @Override
    public Member saveOrUpdateUserNaver(OAuth2User oAuth2User) {
        System.out.printf("hi");
        Optional<Role> role = roleRepository.findById(2L); //USER
        Role defauleRole = role.orElseThrow(() -> new UsernameNotFoundException("Role not found"));
//        System.out.println("oauth" + oAuth2User);
        String naverId = oAuth2User.getAttribute("id");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
//        boolean enabled = oAuth2User.getAttribute("email_verified");

        return memberRepository.findByAccount(naverId)
                .map(member -> {
                    member.setEmail(email);
                    member.setMemberName(name);
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    Member member = new Member();
                    member.setAccount(naverId);
                    member.setEmail(email);
                    member.setMemberName(name);
                    member.setRole(defauleRole); // 기본 권한 설정
                    member.setProvider("Naver");
//                    member.setEnabled(enabled);
                    return memberRepository.save(member);
                });
//        return null;
    }


}
