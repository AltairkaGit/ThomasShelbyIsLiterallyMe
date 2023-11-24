package com.thomas.lib.ws.impl;

import java.security.Principal;

public class PrincipalImpl implements Principal {
    String name;

    PrincipalImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
