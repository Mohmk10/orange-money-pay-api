package com.orangemoney.api.service.impl;

import com.orangemoney.api.common.constants.MessageConstants;
import com.orangemoney.api.dto.response.AccountResponse;
import com.orangemoney.api.dto.response.UserResponse;
import com.orangemoney.api.entity.User;
import com.orangemoney.api.exception.ResourceNotFoundException;
import com.orangemoney.api.mapper.AccountMapper;
import com.orangemoney.api.mapper.UserMapper;
import com.orangemoney.api.repository.UserRepository;
import com.orangemoney.api.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(UserRepository userRepository,
                              UserMapper userMapper,
                              AccountMapper accountMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        UserResponse response = userMapper.toResponse(user);
        if (user.getAccount() != null) {
            response.setAccount(accountMapper.toResponse(user.getAccount()));
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (user.getAccount() == null) {
            throw new ResourceNotFoundException("Aucun compte associé à cet utilisateur");
        }

        return accountMapper.toResponse(user.getAccount());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (user.getAccount() == null) {
            throw new ResourceNotFoundException("Aucun compte associé à cet utilisateur");
        }

        return user.getAccount().getBalance();
    }

    @Override
    @Transactional
    public UserResponse updateProfile(Long userId, String firstName, String lastName, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (firstName != null && !firstName.isBlank()) {
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isBlank()) {
            user.setLastName(lastName);
        }
        if (email != null && !email.isBlank()) {
            if (!email.equals(user.getEmail()) && userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Cet email est déjà utilisé");
            }
            user.setEmail(email);
        }

        User updatedUser = userRepository.save(user);

        UserResponse response = userMapper.toResponse(updatedUser);
        if (updatedUser.getAccount() != null) {
            response.setAccount(accountMapper.toResponse(updatedUser.getAccount()));
        }
        return response;
    }
}