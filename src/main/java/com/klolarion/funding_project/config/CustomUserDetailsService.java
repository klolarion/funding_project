package com.klolarion.funding_project.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klolarion.funding_project.domain.entity.CustomUserDetails;
import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberServiceImpl memberService;

    @Override
    public CustomUserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Member member = memberService.getMember(account);

        return new CustomUserDetails(member);
    }
}
