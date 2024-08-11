package com.klolarion.funding_project.application.port.out;

public interface TokenRepository {
    void setToken(String token);
    String getToken();
    boolean destroyToken();
}
