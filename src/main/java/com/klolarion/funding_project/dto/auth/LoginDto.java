package com.klolarion.funding_project.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String id;
    private String account;
    private String password;
    private String firebaseToken;
}
