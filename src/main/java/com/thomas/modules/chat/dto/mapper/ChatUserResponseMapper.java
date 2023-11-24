package com.thomas.modules.chat.dto.mapper;

import com.thomas.modules.chat.dto.ChatResponseDto;
import com.thomas.modules.chat.dto.ChatUserProfileResponseDto;
import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.service.ChatUserService;
import com.thomas.modules.user.dto.mapper.UserProfileResponseMapper;
import com.thomas.modules.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public class ChatUserResponseMapper {
    @Autowired
    protected ChatUserService chatUserService;

    @Autowired
    protected UserProfileResponseMapper userProfileResponseMapper;

    public ChatUserProfileResponseDto convert(ChatEntity chat, UserEntity user) {
           ChatUserProfileResponseDto res = new ChatUserProfileResponseDto();

           res.setProfile(userProfileResponseMapper.convert(user));
           res.setRoles(chatUserService.getUserChatRoles(chat, user));

           return res;
    }

    public List<ChatUserProfileResponseDto> convertList(ChatEntity chat, List<UserEntity> users) {
        return users.stream().map(user -> convert(chat, user)).collect(Collectors.toList());
    }

    public Page<ChatUserProfileResponseDto> convertPage(ChatEntity chat, Page<UserEntity> page) {
        List<ChatUserProfileResponseDto> list = convertList(chat, page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }
}
