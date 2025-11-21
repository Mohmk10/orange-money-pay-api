package com.orangemoney.api.service.impl;

import com.orangemoney.api.common.constants.MessageConstants;
import com.orangemoney.api.common.enums.RechargeType;
import com.orangemoney.api.common.enums.TransactionStatus;
import com.orangemoney.api.common.util.ReferenceGenerator;
import com.orangemoney.api.dto.request.RechargeRequest;
import com.orangemoney.api.dto.response.RechargeResponse;
import com.orangemoney.api.entity.Account;
import com.orangemoney.api.entity.Recharge;
import com.orangemoney.api.entity.User;
import com.orangemoney.api.exception.InsufficientBalanceException;
import com.orangemoney.api.exception.ResourceNotFoundException;
import com.orangemoney.api.repository.AccountRepository;
import com.orangemoney.api.repository.RechargeRepository;
import com.orangemoney.api.repository.UserRepository;
import com.orangemoney.api.service.RechargeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RechargeServiceImpl implements RechargeService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RechargeRepository rechargeRepository;

    public RechargeServiceImpl(UserRepository userRepository,
                               AccountRepository accountRepository,
                               RechargeRepository rechargeRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.rechargeRepository = rechargeRepository;
    }

    @Override
    @Transactional
    public RechargeResponse performRecharge(Long userId, RechargeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        Account account = user.getAccount();
        if (account == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(MessageConstants.INSUFFICIENT_BALANCE);
        }

        RechargeType type = RechargeType.valueOf(request.getType().toUpperCase());

        Recharge recharge = new Recharge();
        recharge.setReference(ReferenceGenerator.generateTransferReference());
        recharge.setAccount(account);
        recharge.setType(type);
        recharge.setPhoneNumber(request.getPhoneNumber());
        recharge.setAmount(request.getAmount());
        recharge.setStatus(TransactionStatus.COMPLETED);
        recharge.setDescription("Recharge " + type.name() + " - " + request.getPhoneNumber());

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Recharge saved = rechargeRepository.save(recharge);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RechargeResponse> getRechargeHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (user.getAccount() == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        List<Recharge> recharges = rechargeRepository
                .findByAccountIdOrderByCreatedAtDesc(user.getAccount().getId());

        return recharges.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RechargeResponse mapToResponse(Recharge recharge) {
        RechargeResponse response = new RechargeResponse();
        response.setId(recharge.getId());
        response.setReference(recharge.getReference());
        response.setType(recharge.getType().name());
        response.setPhoneNumber(recharge.getPhoneNumber());
        response.setAmount(recharge.getAmount());
        response.setStatus(recharge.getStatus().name());
        response.setDescription(recharge.getDescription());
        response.setCreatedAt(recharge.getCreatedAt());
        return response;
    }
}