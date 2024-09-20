package com.klolarion.funding_project.exception.funding;

import com.klolarion.funding_project.exception.ValidationException;

public class InsufficientFundsException extends ValidationException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
