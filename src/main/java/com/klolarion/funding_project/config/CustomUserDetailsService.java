package com.klolarion.funding_project.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klolarion.funding_project.domain.entity.CustomUserDetails;
import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberServiceImpl memberService;

    //여기서 캐시 조회와 DB조회 후 캐싱까지 구현
    @Override
    public CustomUserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        try {
            Member memberCache = memberService.getMemberCache();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return memberService.findMemberToCustom(account);

    }
}
