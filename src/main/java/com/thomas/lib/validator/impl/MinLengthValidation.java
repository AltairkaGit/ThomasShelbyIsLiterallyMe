package com.thomas.lib.validator.impl;

import com.thomas.lib.validator.AbstractValidationChain;
import com.thomas.lib.validator.exception.LessMinLengthException;

public class MinLengthValidation extends AbstractValidationChain<String> {
    private final int minLength;

    public MinLengthValidation(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public void process(String field, String s) {
        if (s.length() < minLength) throw new LessMinLengthException(field, minLength);
        else if (chain != null) chain.process(field, s);
    }
}
