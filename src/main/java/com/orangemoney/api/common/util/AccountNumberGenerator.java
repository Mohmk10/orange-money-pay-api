package com.orangemoney.api.common.util;

import java.security.SecureRandom;

public class AccountNumberGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String PREFIX = "OM";

    private AccountNumberGenerator() {
    }

    public static String generate() {
        long timestamp = System.currentTimeMillis();
        int randomPart = 1000 + RANDOM.nextInt(9000);
        return PREFIX + timestamp + randomPart;
    }

    public static String generateWithPhoneNumber(String phoneNumber) {
        String cleanPhone = phoneNumber.replaceAll("[^0-9]", "");
        int random = 100 + RANDOM.nextInt(900);
        return PREFIX + cleanPhone + random;
    }
}