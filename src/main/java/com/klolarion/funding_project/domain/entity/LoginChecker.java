package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginChecker extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginCheckerId;
    private String socialProvider;
    private String account;
    private String ipAddress;
}
