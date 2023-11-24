package com.thomas.lib.validator.impl;

import com.thomas.lib.validator.AbstractValidationChain;
import com.thomas.lib.validator.exception.WrongEmailException;

import java.util.regex.Pattern;

public class StrictUnicodeEmailValidation extends AbstractValidationChain<String> {
    private static final Pattern pattern = Pattern.compile("^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$");

    public StrictUnicodeEmailValidation() {
    }

    @Override
    public void process(String field, String email) {
        if (!pattern.matcher(email).matches()) throw new WrongEmailException();
        else if (chain != null) chain.process(field, email);
    }
}
