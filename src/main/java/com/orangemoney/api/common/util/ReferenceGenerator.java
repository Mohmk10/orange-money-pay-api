package com.orangemoney.api.common.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReferenceGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    private ReferenceGenerator() {
    }

    public static String generateTransferReference() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        int random = 1000 + RANDOM.nextInt(9000);
        return "TRF" + timestamp + random;
    }
}