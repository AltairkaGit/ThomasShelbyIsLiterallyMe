package com.thomas.lib.validator.exception;

public class WrongEmailException extends  RuntimeException {
    public WrongEmailException() {
        super("The email has wrong format");
    }
}
