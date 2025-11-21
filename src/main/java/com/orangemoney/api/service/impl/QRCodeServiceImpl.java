package com.orangemoney.api.service.impl;

import com.orangemoney.api.common.constants.MessageConstants;
import com.orangemoney.api.common.enums.TransactionStatus;
import com.orangemoney.api.common.enums.TransactionType;
import com.orangemoney.api.common.util.ReferenceGenerator;
import com.orangemoney.api.dto.request.QRPaymentRequest;
import com.orangemoney.api.dto.response.QRCodeResponse;
import com.orangemoney.api.dto.response.TransferResponse;
import com.orangemoney.api.entity.Account;
import com.orangemoney.api.entity.QRCodeData;
import com.orangemoney.api.entity.Transfer;
import com.orangemoney.api.entity.User;
import com.orangemoney.api.exception.InsufficientBalanceException;
import com.orangemoney.api.exception.ResourceNotFoundException;
import com.orangemoney.api.mapper.TransferMapper;
import com.orangemoney.api.repository.AccountRepository;
import com.orangemoney.api.repository.QRCodeRepository;
import com.orangemoney.api.repository.TransferRepository;
import com.orangemoney.api.repository.UserRepository;
import com.orangemoney.api.service.QRCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    private final UserRepository userRepository;
    private final QRCodeRepository qrCodeRepository;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;

    public QRCodeServiceImpl(UserRepository userRepository,
                             QRCodeRepository qrCodeRepository,
                             AccountRepository accountRepository,
                             TransferRepository transferRepository,
                             TransferMapper transferMapper) {
        this.userRepository = userRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.transferMapper = transferMapper;
    }

    @Override
    @Transactional
    public QRCodeResponse generateUserQRCode(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (user.getAccount() == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        QRCodeData qrCode = new QRCodeData();
        qrCode.setQrToken(UUID.randomUUID().toString());
        qrCode.setType("USER_PAYMENT");
        qrCode.setUser(user);
        qrCode.setExpiresAt(LocalDateTime.now().plusHours(24));

        QRCodeData saved = qrCodeRepository.save(qrCode);

        QRCodeResponse response = new QRCodeResponse();
        response.setQrToken(saved.getQrToken());
        response.setType(saved.getType());
        response.setUserId(user.getId());
        response.setAccountNumber(user.getAccount().getAccountNumber());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setName(user.getFirstName() + " " + user.getLastName());
        response.setExpiresAt(saved.getExpiresAt());

        return response;
    }

    @Override
    @Transactional
    public QRCodeResponse generateMerchantQRCode(Long userId, String amountStr, String reference) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        BigDecimal amount = new BigDecimal(amountStr);

        QRCodeData qrCode = new QRCodeData();
        qrCode.setQrToken(UUID.randomUUID().toString());
        qrCode.setType("MERCHANT_PAYMENT");
        qrCode.setUser(user);
        qrCode.setAmount(amount);
        qrCode.setReference(reference);
        qrCode.setExpiresAt(LocalDateTime.now().plusHours(1));

        QRCodeData saved = qrCodeRepository.save(qrCode);

        QRCodeResponse response = new QRCodeResponse();
        response.setQrToken(saved.getQrToken());
        response.setType(saved.getType());
        response.setUserId(user.getId());
        response.setName(user.getFirstName() + " " + user.getLastName());
        response.setAmount(saved.getAmount());
        response.setReference(saved.getReference());
        response.setExpiresAt(saved.getExpiresAt());

        return response;
    }

    @Override
    @Transactional
    public TransferResponse scanAndPay(Long payerId, QRPaymentRequest request) {
        QRCodeData qrCode = qrCodeRepository.findByQrToken(request.getQrToken())
                .orElseThrow(() -> new ResourceNotFoundException("QR Code invalide"));

        if (qrCode.isExpired()) {
            throw new IllegalStateException("QR Code expiré");
        }

        if (qrCode.isUsed()) {
            throw new IllegalStateException("QR Code déjà utilisé");
        }

        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        User receiver = qrCode.getUser();

        Account payerAccount = payer.getAccount();
        Account receiverAccount = receiver.getAccount();

        BigDecimal amount = qrCode.getAmount() != null ? qrCode.getAmount() : BigDecimal.ZERO;

        if (payerAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(MessageConstants.INSUFFICIENT_BALANCE);
        }

        Transfer transfer = new Transfer();
        transfer.setReference(ReferenceGenerator.generateTransferReference());
        transfer.setSenderAccount(payerAccount);
        transfer.setReceiverAccount(receiverAccount);
        transfer.setAmount(amount);
        transfer.setFees(BigDecimal.ZERO);
        transfer.setTotalAmount(amount);
        transfer.setType(TransactionType.MERCHANT_PAYMENT);
        transfer.setStatus(TransactionStatus.COMPLETED);
        transfer.setDescription("Paiement QR Code - " + qrCode.getReference());

        payerAccount.setBalance(payerAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

        accountRepository.save(payerAccount);
        accountRepository.save(receiverAccount);

        qrCode.setUsed(true);
        qrCodeRepository.save(qrCode);

        Transfer saved = transferRepository.save(transfer);

        return transferMapper.toResponse(saved);
    }
}