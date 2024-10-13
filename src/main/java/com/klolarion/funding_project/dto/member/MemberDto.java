package com.klolarion.funding_project.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long memberId;
    private String role;
    private String email;


    private String nickName;

    private String provider; //공급자 (google, facebook ...)

    private boolean enabled;
    private boolean banned;


    //사용자 등록
    private String memberStatus;
    private int memberStatusCode;
    private LocalDateTime statusExpires;

    public MemberDto(Long memberId, String role, String email, String nickName, String provider, int memberStatusCode, LocalDateTime statusExpires) {
        this.memberId = memberId;
        this.role = role;
        this.email = email;
        this.nickName = nickName;
        this.provider = provider;
        this.memberStatus = memberStatusCode == 1202? "핑크" : memberStatusCode == 1203 ? "실버" : "일반";
        this.statusExpires = statusExpires;
    }
}
