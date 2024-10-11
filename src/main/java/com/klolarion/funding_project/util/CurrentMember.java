package com.klolarion.funding_project.util;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 시큐리티 컨텍스트에서 사용자 정보를 불러와서 사용
 * */
@Component
@RequiredArgsConstructor
public class CurrentMember {
    private final MemberRepository memberRepository;
    public Member getMember(){
        Long memberId = 1L;
        return memberRepository.findById(memberId).orElseThrow(()-> new UsernameNotFoundException("Member not found"));
    }
}
