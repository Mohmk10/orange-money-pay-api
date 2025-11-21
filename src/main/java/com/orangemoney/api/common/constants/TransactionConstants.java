package com.orangemoney.api.common.constants;

import java.math.BigDecimal;

public final class TransactionConstants {

    private TransactionConstants() {
    }

    public static final int FREE_TRANSFERS_PER_DAY = 2;
    public static final BigDecimal FREE_TRANSFER_MIN = new BigDecimal("1");
    public static final BigDecimal FREE_TRANSFER_MAX = new BigDecimal("2000");
    
    public static final BigDecimal DAILY_LIMIT_DEFAULT = new BigDecimal("500000");
    public static final BigDecimal DAILY_LIMIT_KYC_LEVEL_1 = new BigDecimal("200000");
    public static final BigDecimal DAILY_LIMIT_KYC_LEVEL_2 = new BigDecimal("500000");
    public static final BigDecimal DAILY_LIMIT_KYC_LEVEL_3 = new BigDecimal("2000000");
    
    public static final BigDecimal MIN_TRANSFER_AMOUNT = new BigDecimal("100");
    public static final BigDecimal MAX_TRANSFER_AMOUNT = new BigDecimal("1000000");
    
    public static final BigDecimal OTP_THRESHOLD = new BigDecimal("50000");
    
    public static final int CANCELLATION_WINDOW_MINUTES = 5;
    
    public static final int MAX_PIN_ATTEMPTS = 3;
    public static final int PIN_BLOCK_DURATION_HOURS = 24;
}
