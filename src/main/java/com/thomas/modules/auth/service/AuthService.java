package com.thomas.modules.auth.service;

import com.thomas.modules.user.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.CredentialException;

@Transactional
public interface AuthService {
    UserEntity registerUser(UserEntity registringUser) throws CredentialException;
    boolean checkUsernameIsFree(String username);
}
