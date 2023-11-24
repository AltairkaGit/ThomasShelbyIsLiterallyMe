package com.thomas.modules.security.users;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.NoSuchElementException;

public interface UserDetailsService {
    UserDetails loadByUserId(Long userId) throws NoSuchElementException;
}
