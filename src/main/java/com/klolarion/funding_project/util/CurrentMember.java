package com.klolarion.funding_project.util;

import com.klolarion.funding_project.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 캐시에 저장된 사용자 정보를 불러와서 사용
 * */
@Component
@RequiredArgsConstructor
public class CurrentMember {

    public Member getMember(){
        return null;
    }
}
