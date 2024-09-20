package com.klolarion.funding_project.exception.group;


import com.klolarion.funding_project.exception.funding.FundingException;

public class CreateGroupException extends GroupException {
    public CreateGroupException(String message) {
        super(message);
    }
}
