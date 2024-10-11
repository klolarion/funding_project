//6.
package com.klolarion.funding_project.dto.oauth2;

public interface OAuth2Response {

    //제공자 (Ex. naver, google, ...)
    String getProvider();
    //제공자에서 발급해주는 아이디(번호)
    String getUsername();
    //이메일
    String getEmail();
//    //사용자 실명 (설정한 이름)
    String getName();
}
