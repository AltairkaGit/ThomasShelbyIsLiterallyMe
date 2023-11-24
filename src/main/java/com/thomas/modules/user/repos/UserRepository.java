package com.thomas.modules.user.repos;

import com.thomas.modules.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(@Param("username") String username);
    Optional<UserEntity> findByEmail(@Param("email") String Email);
    Set<UserEntity> findAllByUserIdIn(Collection<Long> userIds);
    Set<UserEntity> findAllByUsernameIn(Collection<String> usernames);

}
