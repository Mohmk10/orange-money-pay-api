package com.orangemoney.api.service;

import com.orangemoney.api.dto.request.DepositRequest;
import com.orangemoney.api.dto.request.WithdrawalRequest;
import com.orangemoney.api.dto.response.CashTransactionResponse;

import java.util.List;

public interface CashService {

    CashTransactionResponse requestDeposit(Long userId, DepositRequest request);

    CashTransactionResponse initiateWithdrawal(Long userId, WithdrawalRequest request);

    List<CashTransactionResponse> getCashHistory(Long userId);
}