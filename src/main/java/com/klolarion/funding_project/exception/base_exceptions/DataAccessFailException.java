package com.klolarion.funding_project.exception.base_exceptions;

public class DataAccessFailException extends RuntimeException {
    public DataAccessFailException(String message) {
        super(message);
    }
}
