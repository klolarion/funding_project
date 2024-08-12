package com.klolarion.funding_project.repository;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);

//    /*메일에 인증코드 전송*/
//    boolean sendCodeToEmail();
//    /*인증코드 확인 후 사용자 활성화*/
//    boolean checkCode();
//
//    /*비밀번호 변경*/
//    boolean changePassword();
//
//    /*내 결제수단 목록 조회*/
//    List<PaymentMethodList> myPaymentMethods();
//    /*결제수단 추가*/
//    PaymentMethodList addPaymentMethod();
//    /*주 결제수단으로 지정*/
//    ResponseEntity<?> makeMainPaymentMethod();
//    /*결제수단 삭제*/
//    boolean removePaymentMethod();
//
//    /*로그아웃. 토큰 파괴*/
//    boolean logout();
//    /*회원탈퇴*/
//    boolean leave();
}
