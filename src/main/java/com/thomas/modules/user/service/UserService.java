package com.thomas.modules.user.service;

import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
public interface UserService {
    UserEntity getUserById(Long id) throws NoSuchElementException;
    Set<UserEntity> getUsersByIds(List<Long> ids);
    UserEntity getUserByUsername(String username) throws NoSuchElementException;
    Set<UserEntity> getUsersByUsername(Collection<String> usernames);
    UserEntity updateUsername(UserEntity updatingUser, String username);
    UserEntity updateProfilePicture(UserEntity updatingUser, FileEntity picture);
    UserEntity updatePassword(UserEntity updatingUser, String rawPassword);
    UserEntity updateEmail(UserEntity updatingUser, String email);
    void deleteUser(UserEntity user);
}
