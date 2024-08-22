package com.klolarion.funding_project.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpDto {
    private String account;
    private String serviceName;
    private String code;
}
