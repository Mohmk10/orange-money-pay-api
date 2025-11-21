package com.orangemoney.api.service;

import com.orangemoney.api.dto.request.BankTransferRequest;
import com.orangemoney.api.dto.request.LinkBankAccountRequest;
import com.orangemoney.api.dto.response.BankTransferResponse;
import com.orangemoney.api.dto.response.LinkedBankAccountResponse;

import java.util.List;

public interface BankService {

    LinkedBankAccountResponse linkBankAccount(Long userId, LinkBankAccountRequest request);

    List<LinkedBankAccountResponse> getLinkedAccounts(Long userId);

    void unlinkBankAccount(Long userId, Long bankAccountId);

    BankTransferResponse transferToBank(Long userId, BankTransferRequest request);

    BankTransferResponse transferFromBank(Long userId, BankTransferRequest request);

    List<BankTransferResponse> getBankTransferHistory(Long userId);
}