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
public class CacheDto {
    private Long memberId;
    private String account;
    private String email;
    private String tel;
    private LocalDateTime lastUpdate;
    private boolean enabled;
    private boolean banned;
    private String role;
}
