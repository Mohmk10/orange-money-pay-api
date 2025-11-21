package com.orangemoney.api.service.impl;

import com.orangemoney.api.common.constants.MessageConstants;
import com.orangemoney.api.common.constants.TransactionConstants;
import com.orangemoney.api.common.enums.TransactionStatus;
import com.orangemoney.api.common.enums.TransactionType;
import com.orangemoney.api.common.util.FeeCalculator;
import com.orangemoney.api.common.util.ReferenceGenerator;
import com.orangemoney.api.dto.request.TransferRequest;
import com.orangemoney.api.dto.response.TransferResponse;
import com.orangemoney.api.entity.Account;
import com.orangemoney.api.entity.Transfer;
import com.orangemoney.api.entity.User;
import com.orangemoney.api.exception.DailyLimitExceededException;
import com.orangemoney.api.exception.InsufficientBalanceException;
import com.orangemoney.api.exception.ResourceNotFoundException;
import com.orangemoney.api.mapper.TransferMapper;
import com.orangemoney.api.repository.AccountRepository;
import com.orangemoney.api.repository.TransferRepository;
import com.orangemoney.api.repository.UserRepository;
import com.orangemoney.api.service.TransferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferServiceImpl implements TransferService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;

    public TransferServiceImpl(UserRepository userRepository,
                               AccountRepository accountRepository,
                               TransferRepository transferRepository,
                               TransferMapper transferMapper) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.transferMapper = transferMapper;
    }

    @Override
    @Transactional
    public TransferResponse initiateTransfer(Long senderId, TransferRequest request) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        User receiver = userRepository.findByPhoneNumber(request.getReceiverPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Destinataire introuvable"));

        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("Vous ne pouvez pas transférer vers votre propre compte");
        }

        Account senderAccount = sender.getAccount();
        Account receiverAccount = receiver.getAccount();

        if (senderAccount == null || receiverAccount == null) {
            throw new ResourceNotFoundException("Compte non trouvé");
        }

        long freeTransfersUsed = transferRepository.countFreeTransfersToday(
                senderAccount.getId(),
                TransactionConstants.FREE_TRANSFER_MIN,
                TransactionConstants.FREE_TRANSFER_MAX,
                LocalDate.now().atStartOfDay()
        );

        BigDecimal fees = FeeCalculator.isEligibleForFreeTransfer(
                request.getAmount(),
                freeTransfersUsed,
                TransactionConstants.FREE_TRANSFERS_PER_DAY
        ) ? BigDecimal.ZERO : FeeCalculator.calculateTransferFee(request.getAmount());

        BigDecimal totalAmount = request.getAmount().add(fees);

        if (senderAccount.getBalance().compareTo(totalAmount) < 0) {
            throw new InsufficientBalanceException(MessageConstants.INSUFFICIENT_BALANCE);
        }

        BigDecimal totalSentToday = transferRepository.getTotalSentToday(
                senderAccount.getId(),
                LocalDate.now().atStartOfDay()
        );

        if (totalSentToday.add(totalAmount).compareTo(senderAccount.getDailyLimit()) > 0) {
            throw new DailyLimitExceededException(MessageConstants.DAILY_LIMIT_EXCEEDED);
        }

        Transfer transfer = new Transfer();
        transfer.setReference(ReferenceGenerator.generateTransferReference());
        transfer.setSenderAccount(senderAccount);
        transfer.setReceiverAccount(receiverAccount);
        transfer.setAmount(request.getAmount());
        transfer.setFees(fees);
        transfer.setTotalAmount(totalAmount);
        transfer.setType(TransactionType.TRANSFER);
        transfer.setStatus(TransactionStatus.COMPLETED);
        transfer.setDescription(request.getDescription());

        senderAccount.setBalance(senderAccount.getBalance().subtract(totalAmount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(request.getAmount()));

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        Transfer savedTransfer = transferRepository.save(transfer);

        return transferMapper.toResponse(savedTransfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransferResponse> getTransferHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (user.getAccount() == null) {
            throw new ResourceNotFoundException("Aucun compte associé");
        }

        List<Transfer> transfers = transferRepository.findByAccountId(user.getAccount().getId());

        return transfers.stream()
                .map(transferMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TransferResponse getTransferByReference(String reference) {
        Transfer transfer = transferRepository.findByReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.TRANSACTION_NOT_FOUND));

        return transferMapper.toResponse(transfer);
    }
}