package com.thomas.lib.validator.impl;

import com.thomas.lib.ResponsibilityChain;
import com.thomas.lib.validator.Validator;

public class ValidatorImpl<T> implements Validator<T> {
    private ResponsibilityChain<T> chain;

    public ValidatorImpl(ResponsibilityChain<T> chain) {
        this.chain = chain;
    }

    @Override
    public void validate(String field, T t) {
        chain.process(field, t);
    }
}
