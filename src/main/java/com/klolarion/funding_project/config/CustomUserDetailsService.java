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

    //여기서 캐시 조회와 DB조회 후 캐싱까지 구현
    //필터에서 캐시&DB 조회 후 컨텍스트에 등록. 서비스로직은 컨텍스트에서 사용자정보 호출
    @Override
    public CustomUserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Member member = memberService.getMember(account);

        return new CustomUserDetails(member);
    }
}
