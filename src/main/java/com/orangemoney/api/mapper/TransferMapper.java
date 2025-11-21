package com.orangemoney.api.mapper;

import com.orangemoney.api.config.MapStructConfig;
import com.orangemoney.api.dto.response.TransferResponse;
import com.orangemoney.api.entity.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface TransferMapper {

    @Mapping(target = "senderName", expression = "java(transfer.getSenderAccount().getUser().getFirstName() + \" \" + transfer.getSenderAccount().getUser().getLastName())")
    @Mapping(target = "senderPhone", source = "senderAccount.user.phoneNumber")
    @Mapping(target = "receiverName", expression = "java(transfer.getReceiverAccount().getUser().getFirstName() + \" \" + transfer.getReceiverAccount().getUser().getLastName())")
    @Mapping(target = "receiverPhone", source = "receiverAccount.user.phoneNumber")
    @Mapping(target = "type", expression = "java(transfer.getType().name())")
    @Mapping(target = "status", expression = "java(transfer.getStatus().name())")
    TransferResponse toResponse(Transfer transfer);
}