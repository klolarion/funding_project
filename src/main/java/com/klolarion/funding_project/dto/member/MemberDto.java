package com.klolarion.funding_project.dto.member;

import com.klolarion.funding_project.domain.entity.PaymentMethodList;
import com.klolarion.funding_project.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long memberId;
    private String role;
    private String account;
    private String email;
    private String tel;


    private String memberName;

    private String provider; //공급자 (google, facebook ...)
    private String providerId; //공급 아이디 ?

    private boolean enabled;
    private boolean banned;


    private Long memberStatusId;
    private int memberStatusCode;
    private LocalDateTime statusExpires;
}
