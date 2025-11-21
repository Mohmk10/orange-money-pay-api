package com.orangemoney.api.common.util;

import com.orangemoney.api.common.constants.FeeConstants;

import java.math.BigDecimal;

public class FeeCalculator {

    private FeeCalculator() {
    }

    public static BigDecimal calculateTransferFee(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("5000")) <= 0) {
            return FeeConstants.TRANSFER_FEE_TIER_1;
        } else if (amount.compareTo(new BigDecimal("25000")) <= 0) {
            return FeeConstants.TRANSFER_FEE_TIER_2;
        } else if (amount.compareTo(new BigDecimal("100000")) <= 0) {
            return FeeConstants.TRANSFER_FEE_TIER_3;
        } else {
            return FeeConstants.TRANSFER_FEE_TIER_4;
        }
    }

    public static boolean isEligibleForFreeTransfer(BigDecimal amount, long freeTransfersUsedToday, int maxFreeTransfers) {
        boolean amountInRange = amount.compareTo(new BigDecimal("1")) >= 0
                && amount.compareTo(new BigDecimal("2000")) <= 0;
        boolean hasRemainingFreeTransfers = freeTransfersUsedToday < maxFreeTransfers;

        return amountInRange && hasRemainingFreeTransfers;
    }
}