package com.thomas.modules.auth.dto.mapper;

import com.thomas.modules.auth.dto.AuthRequestDto;
import com.thomas.modules.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AuthRequestMapper extends Converter<AuthRequestDto, UserEntity> {

    UserEntity convert(AuthRequestDto dto);


}
