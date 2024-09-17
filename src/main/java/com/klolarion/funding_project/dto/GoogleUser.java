package com.klolarion.funding_project.dto;

import lombok.Data;

@Data
public class GoogleUser {
    private String id;            // Google의 고유 사용자 ID
    private String email;         // 사용자 이메일
    private String name;          // 사용자 이름
    private String givenName;     // 사용자 이름 (이름 부분만)
    private String familyName;    // 사용자 성 (성 부분만)
    private String picture;       // 프로필 사진 URL
    private String locale;        // 사용자 지역 설정
    private String verifiedEmail; // 이메일 인증 여부
}
