package com.klolarion.funding_project.service.oAuth;

import com.klolarion.funding_project.util.oAuth.OAuth2MemberUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {
    private final OAuth2MemberUpdate oAuth2MemberUpdate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 사용자 속성 매핑을 위한 키 설정
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        String email = null;
        String name = null;

        if ("google".equals(registrationId)) {
            // Google의 경우 기본적으로 제공되는 속성
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");


            System.out.println("google login : " + email + ", " + name);
        } else if ("naver".equals(registrationId)) {
            // Naver의 경우 "response" 키 하위에 사용자 정보가 있음
            attributes = (Map<String, Object>) attributes.get("response");
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            System.out.println("naver login : " + email + ", " + name);
        } else if ("kakao".equals(registrationId)) {
            // Kakao의 경우 "kakao_account" 하위에 사용자 정보가 있음
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            name = (String) profile.get("nickname");

            System.out.println("kakao login : " + email + ", " + name);
        }

        // 유효성 검사 후 null 값 확인
        if (email == null || name == null) {
            throw new OAuth2AuthenticationException("사용자 정보를 가져오지 못했습니다.");
        }

        // 권한 설정
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        OAuth2User user = new DefaultOAuth2User(
                Collections.singleton(authority),
                attributes,
                userNameAttributeName);

        // SecurityContext에 사용자를 설정
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        return user;
    }
}