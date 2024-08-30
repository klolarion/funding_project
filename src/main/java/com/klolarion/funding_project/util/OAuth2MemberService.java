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
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String userNameAttributeName;

        if ("naver".equals(registrationId)) {
//            System.out.println("hi");
            attributes = (Map<String, Object>) attributes.get("response");
            userNameAttributeName = "id";  // 네이버의 경우 id를 식별자로 사용
        } else {
            userNameAttributeName = "email";  // 구글의 경우 email을 식별자로 사용
        }

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        return new DefaultOAuth2User(
                Collections.singleton(authority),
                attributes,
                userNameAttributeName);
    }
}
