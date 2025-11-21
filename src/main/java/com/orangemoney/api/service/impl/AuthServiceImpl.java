package com.orangemoney.api.service.impl;

import com.orangemoney.api.common.constants.MessageConstants;
import com.orangemoney.api.common.constants.TransactionConstants;
import com.orangemoney.api.common.enums.AccountStatus;
import com.orangemoney.api.common.enums.KycLevel;
import com.orangemoney.api.common.util.AccountNumberGenerator;
import com.orangemoney.api.common.util.PinEncoder;
import com.orangemoney.api.dto.request.LoginRequest;
import com.orangemoney.api.dto.request.RegisterRequest;
import com.orangemoney.api.dto.response.AuthResponse;
import com.orangemoney.api.dto.response.UserResponse;
import com.orangemoney.api.entity.Account;
import com.orangemoney.api.entity.User;
import com.orangemoney.api.entity.VerificationToken;
import com.orangemoney.api.exception.InvalidPinException;
import com.orangemoney.api.exception.ResourceNotFoundException;
import com.orangemoney.api.mapper.AccountMapper;
import com.orangemoney.api.mapper.UserMapper;
import com.orangemoney.api.repository.AccountRepository;
import com.orangemoney.api.repository.UserRepository;
import com.orangemoney.api.repository.VerificationTokenRepository;
import com.orangemoney.api.security.JwtTokenProvider;
import com.orangemoney.api.service.AuthService;
import com.orangemoney.api.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserMapper userMapper;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepository,
                           AccountRepository accountRepository,
                           VerificationTokenRepository verificationTokenRepository,
                           UserMapper userMapper,
                           AccountMapper accountMapper,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPin(PinEncoder.encode(request.getPin()));

        User savedUser = userRepository.save(user);

        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(savedUser);
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(token);

        emailService.sendVerificationEmail(savedUser.getEmail(), token.getToken());

        Account account = new Account();
        account.setAccountNumber(AccountNumberGenerator.generateWithPhoneNumber(savedUser.getPhoneNumber()));
        account.setBalance(BigDecimal.ZERO);
        account.setDailyLimit(TransactionConstants.DAILY_LIMIT_DEFAULT);
        account.setKycLevel(KycLevel.LEVEL_0);
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(savedUser);

        Account savedAccount = accountRepository.save(account);

        UserResponse response = userMapper.toResponse(savedUser);
        response.setAccount(accountMapper.toResponse(savedAccount));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPinException("Mot de passe incorrect");
        }

        if (!user.isActive()) {
            throw new IllegalStateException(MessageConstants.ACCOUNT_BLOCKED);
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getPhoneNumber());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        UserResponse userResponse = userMapper.toResponse(user);
        if (user.getAccount() != null) {
            userResponse.setAccount(accountMapper.toResponse(user.getAccount()));
        }

        return new AuthResponse(token, refreshToken, userResponse);
    }

    @Override
    public void logout(String token) {
    }

    @Transactional
    @Override
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token invalide"));

        if (verificationToken.isExpired()) {
            throw new IllegalStateException("Token expiré");
        }

        if (verificationToken.isUsed()) {
            throw new IllegalStateException("Token déjà utilisé");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);
    }

    @Transactional
    @Override
    public void resendVerificationEmail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

        if (user.isVerified()) {
            throw new IllegalStateException("Compte déjà vérifié");
        }

        VerificationToken existingToken = verificationTokenRepository.findByUserId(userId)
                .orElse(null);

        if (existingToken != null && !existingToken.isExpired()) {
            throw new IllegalStateException("Un email de vérification a déjà été envoyé. Veuillez vérifier votre boîte mail.");
        }

        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(token);

        emailService.sendVerificationEmail(user.getEmail(), token.getToken());
    }
}