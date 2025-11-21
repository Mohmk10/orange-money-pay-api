package com.orangemoney.api.service.impl;

import com.orangemoney.api.common.constants.FeeConstants;
import com.orangemoney.api.common.constants.MessageConstants;
import com.orangemoney.api.common.enums.BillCategory;
import com.orangemoney.api.common.enums.TransactionStatus;
import com.orangemoney.api.common.util.ReferenceGenerator;
import com.orangemoney.api.dto.request.BillPaymentRequest;
import com.orangemoney.api.dto.response.BillResponse;
import com.orangemoney.api.entity.Account;
import com.orangemoney.api.entity.Bill;
import com.orangemoney.api.entity.User;
import com.orangemoney.api.exception.InsufficientBalanceException;
import com.orangemoney.api.exception.ResourceNotFoundException;
import com.orangemoney.api.repository.AccountRepository;
import com.orangemoney.api.repository.BillRepository;
import com.orangemoney.api.repository.UserRepository;
import com.orangemoney.api.service.BillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BillRepository billRepository;

    public BillServiceImpl(UserRepository userRepository,
                           AccountRepository accountRepository,
                           BillRepository billRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billRepository = billRepository;
    }

    @Override
    @Transactional
    public BillResponse payBill(Long userId, BillPaymentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        Account account = user.getAccount();
        if (account == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        BillCategory category = BillCategory.valueOf(request.getCategory().toUpperCase());

        BigDecimal fees = calculateFees(category, request.getAmount());
        BigDecimal totalAmount = request.getAmount().add(fees);

        if (account.getBalance().compareTo(totalAmount) < 0) {
            throw new InsufficientBalanceException(MessageConstants.INSUFFICIENT_BALANCE);
        }

        Bill bill = new Bill();
        bill.setReference(ReferenceGenerator.generateTransferReference());
        bill.setAccount(account);
        bill.setCategory(category);
        bill.setProvider(request.getProvider());
        bill.setAccountNumber(request.getAccountNumber());
        bill.setAmount(request.getAmount());
        bill.setFees(fees);
        bill.setTotalAmount(totalAmount);
        bill.setStatus(TransactionStatus.COMPLETED);
        bill.setDescription("Paiement " + request.getProvider() + " - " + request.getAccountNumber());

        account.setBalance(account.getBalance().subtract(totalAmount));
        accountRepository.save(account);

        Bill saved = billRepository.save(bill);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillResponse> getBillHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (user.getAccount() == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        List<Bill> bills = billRepository.findByAccountIdOrderByCreatedAtDesc(user.getAccount().getId());

        return bills.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BigDecimal calculateFees(BillCategory category, BigDecimal amount) {
        return switch (category) {
            case ELECTRICITY, WATER -> amount.multiply(FeeConstants.BILL_PAYMENT_FEE_ELECTRICITY);
            default -> FeeConstants.BILL_PAYMENT_FEE_OTHER;
        };
    }

    private BillResponse mapToResponse(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setReference(bill.getReference());
        response.setCategory(bill.getCategory().name());
        response.setProvider(bill.getProvider());
        response.setAccountNumber(bill.getAccountNumber());
        response.setAmount(bill.getAmount());
        response.setFees(bill.getFees());
        response.setTotalAmount(bill.getTotalAmount());
        response.setStatus(bill.getStatus().name());
        response.setDescription(bill.getDescription());
        response.setCreatedAt(bill.getCreatedAt());
        return response;
    }
}