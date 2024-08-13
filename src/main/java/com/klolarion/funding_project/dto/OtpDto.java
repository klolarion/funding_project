package com.klolarion.funding_project.dto;

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
    private String firebaseToken;
}
