package com.klolarion.funding_project.config;

import com.klolarion.funding_project.domain.entity.CustomUserDetails;
import com.klolarion.funding_project.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberServiceImpl memberService;

    @Override
    public CustomUserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        return memberService.findMemberToCustom(account);

    }
}
