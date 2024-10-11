
package com.klolarion.funding_project.dto.oauth2;

import lombok.Data;

@Data

public class MemberDTO {
    private String role;
    private String name;
    private String provider;
    private String email;
    private Long memberId;
    private String nickName;
}
