package com.klolarion.funding_project.exception.auth;


import com.klolarion.funding_project.exception.ValidationException;

public class LookAccountException extends ValidationException {
    public LookAccountException(String message) {
        super(message);
    }
}
