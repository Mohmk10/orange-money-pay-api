package com.orangemoney.api.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PinEncoder {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private PinEncoder() {
    }

    public static String encode(String pin) {
        return ENCODER.encode(pin);
    }

    public static boolean matches(String rawPin, String encodedPin) {
        return ENCODER.matches(rawPin, encodedPin);
    }
}