//8.
package com.klolarion.funding_project.dto.oauth2;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {

    private  final Map<String,Object> attribute;
    public GoogleResponse(Map<String,Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "google";
    }


    @Override
    public String getUsername() {
        // Google OAuth2에서 사용자 고유 ID는 "sub" 필드에 있음
        Object sub = attribute.get("sub");
        if (sub == null) {
            throw new NullPointerException("Google OAuth2 response does not contain 'sub' field.");
        }
        return sub.toString();
    }

    @Override
    public String getEmail() {
        Object email = attribute.get("email");
        if (email == null) {
            throw new NullPointerException("Google OAuth2 response does not contain 'email' field.");
        }
        return email.toString();
    }

    @Override
    public String getName() {
        Object name = attribute.get("name");
        if (name == null) {
            throw new NullPointerException("Google OAuth2 response does not contain 'name' field.");
        }
        return name.toString();
    }
}
