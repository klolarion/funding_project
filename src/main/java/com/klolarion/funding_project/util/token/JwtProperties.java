package com.klolarion.funding_project.util.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProperties {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access-token-remain-seconds}")
    private long accessTokenRemainSeconds;
    @Value("${application.security.jwt.refresh-token-remain-seconds}")
    private long refreshTokenRemainSeconds;

    // Getters and Setters
    public String getSecretKey() {
        return secretKey;
    }

    public long getAccessTokenRemainSeconds() {
        return accessTokenRemainSeconds;
    }

    public long getRefreshTokenRemainSeconds() {
        return refreshTokenRemainSeconds;
    }

    public void setAccessTokenRemainSeconds(long accessTokenRemainSeconds) {
        this.accessTokenRemainSeconds = accessTokenRemainSeconds;
    }

    public void setRefreshTokenValidityInSeconds(long refreshTokenRemainSeconds) {
        this.refreshTokenRemainSeconds = refreshTokenRemainSeconds;
    }
}
