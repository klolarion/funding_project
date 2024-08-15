package com.klolarion.funding_project.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberInfoUpdateDto {
    private String email;
    private String tel;
//    private String zoneCode;
//    private String address1;
//    private String address2;
    private LocalDateTime lastSendDate;

}
