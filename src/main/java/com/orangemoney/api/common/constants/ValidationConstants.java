package com.orangemoney.api.common.constants;

public final class ValidationConstants {

    private ValidationConstants() {
    }

    public static final String PHONE_NUMBER_REGEX = "^(77|78|76|70)\\d{7}$";
    public static final String PIN_REGEX = "^\\d{4}$";
    
    public static final String[] FORBIDDEN_PINS = {"0000", "1234", "4321", "1111", "2222"};
    
    public static final int OTP_LENGTH = 6;
    public static final int OTP_VALIDITY_MINUTES = 5;
    
    public static final int MIN_PASSWORD_LENGTH = 8;
}
