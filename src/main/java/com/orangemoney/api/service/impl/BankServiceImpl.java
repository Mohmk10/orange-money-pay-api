package com.orangemoney.api.service.impl;

import com.orangemoney.api.common.constants.MessageConstants;
import com.orangemoney.api.common.enums.TransactionStatus;
import com.orangemoney.api.common.util.ReferenceGenerator;
import com.orangemoney.api.dto.request.BankTransferRequest;
import com.orangemoney.api.dto.request.LinkBankAccountRequest;
import com.orangemoney.api.dto.response.BankTransferResponse;
import com.orangemoney.api.dto.response.LinkedBankAccountResponse;
import com.orangemoney.api.entity.Account;
import com.orangemoney.api.entity.BankTransfer;
import com.orangemoney.api.entity.LinkedBankAccount;
import com.orangemoney.api.entity.User;
import com.orangemoney.api.exception.InsufficientBalanceException;
import com.orangemoney.api.exception.ResourceNotFoundException;
import com.orangemoney.api.repository.AccountRepository;
import com.orangemoney.api.repository.BankTransferRepository;
import com.orangemoney.api.repository.LinkedBankAccountRepository;
import com.orangemoney.api.repository.UserRepository;
import com.orangemoney.api.service.BankService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl implements BankService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final LinkedBankAccountRepository linkedBankAccountRepository;
    private final BankTransferRepository bankTransferRepository;

    public BankServiceImpl(UserRepository userRepository,
                           AccountRepository accountRepository,
                           LinkedBankAccountRepository linkedBankAccountRepository,
                           BankTransferRepository bankTransferRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.linkedBankAccountRepository = linkedBankAccountRepository;
        this.bankTransferRepository = bankTransferRepository;
    }

    @Override
    @Transactional
    public LinkedBankAccountResponse linkBankAccount(Long userId, LinkBankAccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        LinkedBankAccount linkedAccount = new LinkedBankAccount();
        linkedAccount.setUser(user);
        linkedAccount.setBankName(request.getBankName());
        linkedAccount.setAccountNumber(request.getAccountNumber());
        linkedAccount.setAccountHolderName(request.getAccountHolderName());
        linkedAccount.setActive(true);

        LinkedBankAccount saved = linkedBankAccountRepository.save(linkedAccount);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LinkedBankAccountResponse> getLinkedAccounts(Long userId) {
        List<LinkedBankAccount> accounts = linkedBankAccountRepository.findByUserId(userId);
        return accounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void unlinkBankAccount(Long userId, Long bankAccountId) {
        LinkedBankAccount linkedAccount = linkedBankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Compte bancaire non trouvé"));

        if (!linkedAccount.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Ce compte ne vous appartient pas");
        }

        linkedAccount.setActive(false);
        linkedBankAccountRepository.save(linkedAccount);
    }

    @Override
    @Transactional
    public BankTransferResponse transferToBank(Long userId, BankTransferRequest request) {
        return performBankTransfer(userId, request, "TO_BANK");
    }

    @Override
    @Transactional
    public BankTransferResponse transferFromBank(Long userId, BankTransferRequest request) {
        return performBankTransfer(userId, request, "FROM_BANK");
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankTransferResponse> getBankTransferHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (user.getAccount() == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        List<BankTransfer> transfers = bankTransferRepository
                .findByAccountIdOrderByCreatedAtDesc(user.getAccount().getId());

        return transfers.stream()
                .map(this::mapToBankTransferResponse)
                .collect(Collectors.toList());
    }

    private BankTransferResponse performBankTransfer(Long userId, BankTransferRequest request, String direction) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        Account account = user.getAccount();
        if (account == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        LinkedBankAccount linkedAccount = linkedBankAccountRepository.findById(request.getLinkedBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Compte bancaire lié non trouvé"));

        if (!linkedAccount.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Ce compte bancaire ne vous appartient pas");
        }

        if (!linkedAccount.isActive()) {
            throw new IllegalArgumentException("Ce compte bancaire n'est plus actif");
        }

        BigDecimal fees = BigDecimal.ZERO;
        BigDecimal totalAmount = request.getAmount().add(fees);

        if ("TO_BANK".equals(direction)) {
            if (account.getBalance().compareTo(totalAmount) < 0) {
                throw new InsufficientBalanceException(MessageConstants.INSUFFICIENT_BALANCE);
            }
            account.setBalance(account.getBalance().subtract(totalAmount));
        } else {
            account.setBalance(account.getBalance().add(request.getAmount()));
        }

        BankTransfer transfer = new BankTransfer();
        transfer.setReference(ReferenceGenerator.generateTransferReference());
        transfer.setAccount(account);
        transfer.setLinkedBankAccount(linkedAccount);
        transfer.setDirection(direction);
        transfer.setAmount(request.getAmount());
        transfer.setFees(fees);
        transfer.setTotalAmount(totalAmount);
        transfer.setStatus(TransactionStatus.COMPLETED);
        transfer.setDescription("Transfert " + direction + " - " + linkedAccount.getBankName());

        accountRepository.save(account);
        BankTransfer saved = bankTransferRepository.save(transfer);

        return mapToBankTransferResponse(saved);
    }

    private LinkedBankAccountResponse mapToResponse(LinkedBankAccount account) {
        LinkedBankAccountResponse response = new LinkedBankAccountResponse();
        response.setId(account.getId());
        response.setBankName(account.getBankName());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountHolderName(account.getAccountHolderName());
        response.setActive(account.isActive());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }

    private BankTransferResponse mapToBankTransferResponse(BankTransfer transfer) {
        BankTransferResponse response = new BankTransferResponse();
        response.setId(transfer.getId());
        response.setReference(transfer.getReference());
        response.setBankName(transfer.getLinkedBankAccount().getBankName());
        response.setAccountNumber(transfer.getLinkedBankAccount().getAccountNumber());
        response.setDirection(transfer.getDirection());
        response.setAmount(transfer.getAmount());
        response.setFees(transfer.getFees());
        response.setTotalAmount(transfer.getTotalAmount());
        response.setStatus(transfer.getStatus().name());
        response.setDescription(transfer.getDescription());
        response.setCreatedAt(transfer.getCreatedAt());
        return response;
    }
}