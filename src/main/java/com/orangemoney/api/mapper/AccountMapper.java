package com.orangemoney.api.mapper;

import com.orangemoney.api.config.MapStructConfig;
import com.orangemoney.api.dto.response.AccountResponse;
import com.orangemoney.api.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface AccountMapper {

    @Mapping(target = "kycLevel", expression = "java(account.getKycLevel().name())")
    @Mapping(target = "status", expression = "java(account.getStatus().name())")
    AccountResponse toResponse(Account account);
}