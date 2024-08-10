package com.klolarion.funding_project.application.service;

import com.klolarion.funding_project.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationMemberService {
    private final MemberRepository memberRepository;

}
