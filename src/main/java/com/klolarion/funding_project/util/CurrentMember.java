package com.klolarion.funding_project.util;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 시큐리티 컨텍스트에서 사용자 정보를 불러와서 사용
 * */
@Component
@RequiredArgsConstructor
public class CurrentMember {
    private final MemberRepository memberRepository;
    public Member getMember(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String account;
        String email;
        if (principal instanceof UserDetails) {
            account = ((UserDetails) principal).getUsername();
            Optional<Member> findMember = memberRepository.findByAccount(account);
            return findMember.orElseThrow(() -> new UsernameNotFoundException("사용자 조회 실패"));
        } else if (principal instanceof OAuth2User) {
            //oAuth는 이메일로 사용자 조회
            email = ((OAuth2User) principal).getAttribute("email");
            Optional<Member> findMember = memberRepository.findByEmail(email);
            return findMember.orElseThrow(() -> new UsernameNotFoundException("사용자 조회 실패"));
        } else {
            account = principal.toString();
            Optional<Member> findMember = memberRepository.findByAccount(account);
            return findMember.orElseThrow(() -> new UsernameNotFoundException("사용자 조회 실패"));
        }


    }
}
