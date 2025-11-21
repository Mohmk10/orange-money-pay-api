package com.orangemoney.api.service;

import com.orangemoney.api.dto.request.TransferRequest;
import com.orangemoney.api.dto.response.TransferResponse;

import java.util.List;

public interface TransferService {

    TransferResponse initiateTransfer(Long senderId, TransferRequest request);

    List<TransferResponse> getTransferHistory(Long userId);

    TransferResponse getTransferByReference(String reference);
}