package com.klolarion.funding_project.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class MemberCacheDto {
    private Long memberId;
    private String email;
    private boolean enabled;
    private boolean banned;
    private String role;

}
