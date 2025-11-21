package com.orangemoney.api.common.constants;

import java.math.BigDecimal;

public final class FeeConstants {

    private FeeConstants() {
    }

    public static final BigDecimal TRANSFER_FEE_TIER_1 = new BigDecimal("50");
    public static final BigDecimal TRANSFER_FEE_TIER_2 = new BigDecimal("100");
    public static final BigDecimal TRANSFER_FEE_TIER_3 = new BigDecimal("200");
    public static final BigDecimal TRANSFER_FEE_TIER_4 = new BigDecimal("500");
    
    public static final BigDecimal WITHDRAWAL_FEE_PERCENTAGE = new BigDecimal("0.01");
    public static final BigDecimal WITHDRAWAL_FEE_MIN = new BigDecimal("100");
    
    public static final BigDecimal BILL_PAYMENT_FEE_ELECTRICITY = new BigDecimal("0.01");
    public static final BigDecimal BILL_PAYMENT_FEE_WATER = new BigDecimal("0.01");
    public static final BigDecimal BILL_PAYMENT_FEE_OTHER = BigDecimal.ZERO;
    
    public static final BigDecimal INTERNATIONAL_TRANSFER_FEE = new BigDecimal("1000");
}
