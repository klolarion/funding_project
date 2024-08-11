package com.klolarion.funding_project.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CsrfToken {

    @Id
    private String account;
    private String token;

}
