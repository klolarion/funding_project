package com.klolarion.funding_project.dto.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final MemberDTO memberDTO;
    public CustomOAuth2User(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return null; // 구글과 네이버의 attributes의 값들이 다르기 때문에 null 처리
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(memberDTO.getRole()));  // 사용자 역할 반환
        return authorities;
    }

    @Override
    public String getName() {
        return  memberDTO.getName();
    }

    public String getNickName() {
        return  memberDTO.getNickName();
    }

    public Long getMemberId() {return memberDTO.getMemberId();}

}
