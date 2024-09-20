package com.klolarion.funding_project.service.blueprint;

import com.klolarion.funding_project.domain.entity.Member;
import com.klolarion.funding_project.dto.auth.RegisterDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {
    /*인증서버에서 받아온 가입정보로 사용자 생성*/
    boolean register(RegisterDto registerDto);

    /*계정찾기*/
    String findAccount(String tel);

    /*가입시 계정중복 확인*/
    void lookAccount(String account);
    /*가입시 전화번호중복 확인*/
    void lookTel(String tel);
}
