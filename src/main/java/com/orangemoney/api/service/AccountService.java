package com.orangemoney.api.service;

import com.orangemoney.api.dto.response.AccountResponse;
import com.orangemoney.api.dto.response.UserResponse;

import java.math.BigDecimal;

public interface AccountService {

    UserResponse getProfile(Long userId);

    AccountResponse getAccountDetails(Long userId);

    BigDecimal getBalance(Long userId);

    UserResponse updateProfile(Long userId, String firstName, String lastName, String email);
}