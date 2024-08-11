package com.klolarion.funding_project.infrastructure.persistence;

import com.klolarion.funding_project.application.port.out.TokenRepository;

public class RedisTokenRepositoryImpl implements TokenRepository {
    @Override
    public void setToken(String token) {

    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public boolean destroyToken() {
        return false;
    }
}
