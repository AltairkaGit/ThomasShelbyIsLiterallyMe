package com.thomas.lib.validator.exception;

public class LessMinLengthException extends  RuntimeException {
    public LessMinLengthException(String field, int len) {
        super(field + ": Length is less than min length, min len is: " + len);
    }
}
