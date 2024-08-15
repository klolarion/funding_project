package com.klolarion.funding_project.service.blueprint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import com.klolarion.funding_project.dto.auth.RegisterDto;

import java.util.List;

public interface MemberService {

    /*인증서버에서 받아온 가입정보로 사용자 생성*/
    boolean save(RegisterDto registerDto);

    /*멤버정보 캐시등록*/
    Member getMemberCache() throws JsonProcessingException;

    /*이름으로 사용자 검색*/
    List<Member>  searchMember(String memberName);

    /*캐시에서 멤버정보 조회 => 실패시 DB에서 조회 후 리턴*/
    void setMemberCache(Member member);

    /*내 정보*/
    Member myInfo();

    /*내 결제수단 목록*/
    List<PaymentMethodList> myPaymentLists(Long memberId);

    /*결제수단 추가*/
    PaymentMethodList addPaymentMethod(Long paymentMethodId);

    /*주 결제수단으로 등록*/
    boolean makeMainPayment(Long paymentMethodListId);

    /*주 결제수단 조회*/
    PaymentMethodList getMainPaymentMethod();

    /*내 결제수단 삭제*/
    boolean removePayment(Long paymentMethodListId);

    /*로그아웃*/
    boolean logout();

    /*회원탈퇴*/
    boolean leave();
}
