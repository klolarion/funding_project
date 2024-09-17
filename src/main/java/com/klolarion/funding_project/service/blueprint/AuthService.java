package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.auth.RegisterDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {
    /*인증서버에서 받아온 가입정보로 사용자 생성*/
    boolean register(RegisterDto registerDto);

    /*계정찾기*/
    String findAccount(String tel);

    /*구글로그인 테스트용*/
    Member saveOrUpdateUserGoogle(OAuth2User oAuth2User);
    /*네이버로그인 테스트용*/
    Member saveOrUpdateUserNaver(OAuth2User oAuth2User);
    /*카카오로그인 테스트용*/
    Member saveOrUpdateUserKakao(OAuth2User oAuth2User);

    /*가입시 계정중복 확인*/
    boolean lookAccount(String account);
    /*가입시 전화번호중복 확인*/
    boolean lookTel(String tel);
}
