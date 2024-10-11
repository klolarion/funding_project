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
    private String email;


    private String nickName;

    private String provider; //공급자 (google, facebook ...)

    private boolean enabled;
    private boolean banned;


    //사용자 등록
    private Long memberStatusId;
    private int memberStatusCode;
    private LocalDateTime statusExpires;
}
