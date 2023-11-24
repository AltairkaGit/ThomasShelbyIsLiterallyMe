package com.thomas.modules.security.users;

import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.user.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadByUserId(Long userId)  {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new NoSuchElementException("user not found to construct details");
        return UserDetailsFactory.create(user.get());
    }
}
