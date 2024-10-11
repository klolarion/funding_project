package com.klolarion.funding_project.dto.oauth2;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao"; // 카카오 로그인 제공자
    }

    @Override
    public String getUsername() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null && kakaoAccount.get("email") != null) {
            return kakaoAccount.get("email").toString();
        }
        return null;
    }

    @Override
    public String getName() {
        // 'kakao_account'에서 'profile' 정보를 가져옴
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                return profile.get("nickname").toString();  // 닉네임을 가져옴
            }
        }
        return null; // 닉네임이 없을 경우 null 반환
    }

}