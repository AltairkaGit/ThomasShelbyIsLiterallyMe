package com.thomas.lib.validator.exception;

public class MoreMaxLengthException extends  RuntimeException {
    public  MoreMaxLengthException(String field, int len) {
        super(field + ": More than max length, max len is: " + len);
    }
}
