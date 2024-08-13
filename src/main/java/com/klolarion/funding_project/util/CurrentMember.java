package com.klolarion.funding_project.util;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 캐시에 저장된 사용자 정보를 불러와서 사용
 * */
@Component
@RequiredArgsConstructor
public class CurrentMember {
    private final MemberRepository memberRepository;
    public Member getMember(){
        String account = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<Member> findMember = memberRepository.findByAccount(account);
        return findMember.orElseThrow(() -> new UsernameNotFoundException("사용자 조회 실패"));
    }
}
