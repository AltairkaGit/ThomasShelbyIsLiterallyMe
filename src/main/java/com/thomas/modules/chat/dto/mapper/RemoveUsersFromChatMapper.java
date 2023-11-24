package com.thomas.modules.chat.dto.mapper;

import com.thomas.modules.chat.dto.RemoveUsersFromChatRequestDto;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.user.repos.UserRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Mapper(componentModel = "spring")
public abstract class RemoveUsersFromChatMapper {
    @Autowired
    protected UserRepository userRepository;
    public Set<UserEntity> convert(RemoveUsersFromChatRequestDto dto) {
        return userRepository.findAllByUserIdIn(dto.getUserIds());
    }
}
