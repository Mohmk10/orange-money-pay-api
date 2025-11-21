package com.orangemoney.api.mapper;

import com.orangemoney.api.config.MapStructConfig;
import com.orangemoney.api.dto.request.RegisterRequest;
import com.orangemoney.api.dto.response.UserResponse;
import com.orangemoney.api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = {AccountMapper.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "role", constant = "ROLE_USER")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "verified", constant = "false")
    @Mapping(target = "pinAttempts", constant = "0")
    @Mapping(target = "pinBlockedUntil", ignore = true)
    User toEntity(RegisterRequest request);

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserResponse toResponse(User user);
}