package com.orangemoney.api.service.impl;

import com.orangemoney.api.common.constants.MessageConstants;
import com.orangemoney.api.common.enums.TransactionStatus;
import com.orangemoney.api.common.enums.TransactionType;
import com.orangemoney.api.common.util.ReferenceGenerator;
import com.orangemoney.api.dto.request.DepositRequest;
import com.orangemoney.api.dto.request.WithdrawalRequest;
import com.orangemoney.api.dto.response.CashTransactionResponse;
import com.orangemoney.api.entity.Account;
import com.orangemoney.api.entity.CashTransaction;
import com.orangemoney.api.entity.User;
import com.orangemoney.api.exception.InsufficientBalanceException;
import com.orangemoney.api.exception.ResourceNotFoundException;
import com.orangemoney.api.repository.AccountRepository;
import com.orangemoney.api.repository.CashTransactionRepository;
import com.orangemoney.api.repository.UserRepository;
import com.orangemoney.api.service.CashService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashServiceImpl implements CashService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CashTransactionRepository cashTransactionRepository;

    public CashServiceImpl(UserRepository userRepository,
                           AccountRepository accountRepository,
                           CashTransactionRepository cashTransactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.cashTransactionRepository = cashTransactionRepository;
    }

    @Override
    @Transactional
    public CashTransactionResponse requestDeposit(Long userId, DepositRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        Account account = user.getAccount();
        if (account == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        CashTransaction transaction = new CashTransaction();
        transaction.setReference(ReferenceGenerator.generateTransferReference());
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setAgentCode(request.getAgentCode());
        transaction.setDescription("Dépôt chez agent " + request.getAgentCode());

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        CashTransaction saved = cashTransactionRepository.save(transaction);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public CashTransactionResponse initiateWithdrawal(Long userId, WithdrawalRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        Account account = user.getAccount();
        if (account == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(MessageConstants.INSUFFICIENT_BALANCE);
        }

        CashTransaction transaction = new CashTransaction();
        transaction.setReference(ReferenceGenerator.generateTransferReference());
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setAgentCode(request.getAgentCode());
        transaction.setDescription("Retrait chez agent " + request.getAgentCode());

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        CashTransaction saved = cashTransactionRepository.save(transaction);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashTransactionResponse> getCashHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (user.getAccount() == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        List<CashTransaction> transactions = cashTransactionRepository
                .findByAccountIdOrderByCreatedAtDesc(user.getAccount().getId());

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CashTransactionResponse mapToResponse(CashTransaction transaction) {
        CashTransactionResponse response = new CashTransactionResponse();
        response.setId(transaction.getId());
        response.setReference(transaction.getReference());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType().name());
        response.setStatus(transaction.getStatus().name());
        response.setAgentCode(transaction.getAgentCode());
        response.setDescription(transaction.getDescription());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }
}