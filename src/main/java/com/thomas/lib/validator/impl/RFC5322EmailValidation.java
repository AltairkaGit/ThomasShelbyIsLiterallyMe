package com.thomas.lib.validator.impl;

import com.thomas.lib.validator.AbstractValidationChain;
import com.thomas.lib.validator.exception.WrongEmailException;

import java.util.regex.Pattern;

public class RFC5322EmailValidation extends AbstractValidationChain<String> {
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public RFC5322EmailValidation() {
    }

    @Override
    public void process(String field, String email) {
        if (!pattern.matcher(email).matches()) throw new WrongEmailException();
        else if (chain != null) chain.process(field, email);
    }
}
