package com.orangemoney.api.service.impl;

import com.orangemoney.api.common.constants.MessageConstants;
import com.orangemoney.api.common.enums.TransactionStatus;
import com.orangemoney.api.common.util.ReferenceGenerator;
import com.orangemoney.api.dto.request.MerchantPaymentRequest;
import com.orangemoney.api.dto.response.MerchantPaymentResponse;
import com.orangemoney.api.entity.Account;
import com.orangemoney.api.entity.MerchantPayment;
import com.orangemoney.api.entity.User;
import com.orangemoney.api.exception.InsufficientBalanceException;
import com.orangemoney.api.exception.ResourceNotFoundException;
import com.orangemoney.api.repository.AccountRepository;
import com.orangemoney.api.repository.MerchantPaymentRepository;
import com.orangemoney.api.repository.UserRepository;
import com.orangemoney.api.service.MerchantPaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantPaymentServiceImpl implements MerchantPaymentService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final MerchantPaymentRepository merchantPaymentRepository;

    public MerchantPaymentServiceImpl(UserRepository userRepository,
                                      AccountRepository accountRepository,
                                      MerchantPaymentRepository merchantPaymentRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.merchantPaymentRepository = merchantPaymentRepository;
    }

    @Override
    @Transactional
    public MerchantPaymentResponse payMerchant(Long payerId, MerchantPaymentRequest request) {
        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        User merchant = userRepository.findByPhoneNumber(request.getMerchantPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Marchand introuvable"));

        Account payerAccount = payer.getAccount();
        Account merchantAccount = merchant.getAccount();

        if (payerAccount == null || merchantAccount == null) {
            throw new ResourceNotFoundException("Compte non trouvé");
        }

        BigDecimal fees = BigDecimal.ZERO;
        BigDecimal totalAmount = request.getAmount().add(fees);

        if (payerAccount.getBalance().compareTo(totalAmount) < 0) {
            throw new InsufficientBalanceException(MessageConstants.INSUFFICIENT_BALANCE);
        }

        MerchantPayment payment = new MerchantPayment();
        payment.setReference(ReferenceGenerator.generateTransferReference());
        payment.setPayerAccount(payerAccount);
        payment.setMerchantAccount(merchantAccount);
        payment.setMerchantName(merchant.getFirstName() + " " + merchant.getLastName());
        payment.setAmount(request.getAmount());
        payment.setFees(fees);
        payment.setTotalAmount(totalAmount);
        payment.setStatus(TransactionStatus.COMPLETED);
        payment.setDescription(request.getDescription());

        payerAccount.setBalance(payerAccount.getBalance().subtract(totalAmount));
        merchantAccount.setBalance(merchantAccount.getBalance().add(request.getAmount()));

        accountRepository.save(payerAccount);
        accountRepository.save(merchantAccount);

        MerchantPayment saved = merchantPaymentRepository.save(payment);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantPaymentResponse> getMerchantPaymentHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (user.getAccount() == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        List<MerchantPayment> payments = merchantPaymentRepository
                .findByAccountId(user.getAccount().getId());

        return payments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MerchantPaymentResponse mapToResponse(MerchantPayment payment) {
        MerchantPaymentResponse response = new MerchantPaymentResponse();
        response.setId(payment.getId());
        response.setReference(payment.getReference());
        response.setPayerName(payment.getPayerAccount().getUser().getFirstName() + " " +
                payment.getPayerAccount().getUser().getLastName());
        response.setMerchantName(payment.getMerchantName());
        response.setAmount(payment.getAmount());
        response.setFees(payment.getFees());
        response.setTotalAmount(payment.getTotalAmount());
        response.setStatus(payment.getStatus().name());
        response.setDescription(payment.getDescription());
        response.setCreatedAt(payment.getCreatedAt());
        return response;
    }
}