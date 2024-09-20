package com.klolarion.funding_project.exception;

import com.klolarion.funding_project.exception.auth.AuthException;

public class ValidationException extends AuthException {
    public ValidationException(String message) {
        super(message);
    }
}
