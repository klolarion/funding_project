package com.klolarion.funding_project.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class OAuth2MemberService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
//        System.out.println("oAuth2User = " + oAuth2User.getAttributes());

        // 구글에서 가져온 사용자 정보
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        // Spring Security의 컨텍스트에 등록할 OAuth2User 객체 생성
        return new DefaultOAuth2User(
                Collections.singleton(authority),
                attributes,
                "email");
    }
}
