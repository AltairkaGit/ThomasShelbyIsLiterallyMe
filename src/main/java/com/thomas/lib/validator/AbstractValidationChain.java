package com.thomas.lib.validator;

import com.thomas.lib.ResponsibilityChain;

public abstract class AbstractValidationChain<T> implements ResponsibilityChain<T> {
    protected ResponsibilityChain<T> chain;

    @Override
    public ResponsibilityChain<T> setNextChain(ResponsibilityChain<T> nextChain) {
        chain = nextChain;
        return this;
    }

}
