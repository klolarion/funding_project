package com.klolarion.funding_project.exception.funding;

import com.klolarion.funding_project.exception.auth.AuthException;

public class InsufficientFundsException extends AuthException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
