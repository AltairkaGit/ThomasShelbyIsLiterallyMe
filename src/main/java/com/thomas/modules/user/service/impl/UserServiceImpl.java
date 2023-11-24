package com.thomas.modules.user.service.impl;

import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.user.repos.UserRepository;
import com.thomas.modules.auth.service.AuthService;
import com.thomas.modules.user.service.UserService;
import com.thomas.lib.validator.impl.MaxLengthValidation;
import com.thomas.lib.validator.impl.MinLengthValidation;
import com.thomas.lib.validator.impl.StrictUnicodeEmailValidation;
import com.thomas.lib.validator.Validator;
import com.thomas.lib.validator.impl.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService, AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity getUserById(Long id) throws NoSuchElementException {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NoSuchElementException();
        return user.get();
    }

    @Override
    public Set<UserEntity> getUsersByIds(List<Long> ids) {
        return userRepository.findAllByUserIdIn(ids);
    }

    @Override
    public UserEntity getUserByUsername(String userName) throws NoSuchElementException {
        Optional<UserEntity> user = userRepository.findByUsername(userName);
        if (user.isEmpty()) throw new NoSuchElementException();
        return user.get();
    }

    @Override
    public Set<UserEntity> getUsersByUsername(Collection<String> usernames) {
        return userRepository.findAllByUsernameIn(usernames);
    }

    @Override
    public UserEntity updateUsername(UserEntity updatingUser, String username) {
        updatingUser.setUsername(username);
        return userRepository.saveAndFlush(updatingUser);
    }

    @Override
    public UserEntity updateProfilePicture(UserEntity updatingUser, FileEntity picture) {
        updatingUser.setProfilePicture(picture);
        return userRepository.saveAndFlush(updatingUser);
    }

    @Override
    public UserEntity updatePassword(UserEntity updatingUser, String rawPassword) {
        validatePassword(rawPassword);
        updatingUser.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.saveAndFlush(updatingUser);
    }

    @Override
    public UserEntity updateEmail(UserEntity updatingUser, String email) {
        validateEmail(email);
        updatingUser.setEmail(email);
        return userRepository.saveAndFlush(updatingUser);
    }

    @Override
    public UserEntity registerUser(UserEntity registeringUser) throws CredentialException {
        validateName(registeringUser.getUsername());
        validateEmail(registeringUser.getEmail());
        validatePassword(registeringUser.getPassword());
        if (userRepository.findByUsername(registeringUser.getUsername()).isPresent())
            throw new CredentialException("this username is taken by someone");
        if (userRepository.findByEmail(registeringUser.getEmail()).isPresent())
            throw new CredentialException("this email is taken by someone");
        UserEntity user = new UserEntity();
        user.setUsername(registeringUser.getUsername());
        user.setEmail(registeringUser.getEmail());
        user.setGender(registeringUser.getGender());
        user.setPassword(passwordEncoder.encode(registeringUser.getPassword()));

        userRepository.saveAndFlush(user);
        return user;
    }

    @Override
    public boolean checkUsernameIsFree(String username) {
        validateName(username);
        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public void deleteUser(UserEntity user) {
        userRepository.deleteById(user.getUserId());
    }

    private void validateEmail(String email) {
        Validator<String> emailValidator = new ValidatorImpl<>(new StrictUnicodeEmailValidation());
        emailValidator.validate("Email", email);
    }

    private void validatePassword(String password) {
        Validator<String> passwordValidator = new ValidatorImpl<>(
                new MinLengthValidation(8).setNextChain(
                new MaxLengthValidation(50)
            ));
        passwordValidator.validate("Password", password);
    }

    private void validateName(String name) {
        Validator<String> nameValidator = new ValidatorImpl<>(
                new MinLengthValidation(2).setNextChain(
                        new MaxLengthValidation(25)
                ));
        nameValidator.validate("username", name);
    }
}
