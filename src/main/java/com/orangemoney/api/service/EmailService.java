package com.orangemoney.api.service;

public interface EmailService {

    void sendVerificationEmail(String email, String token);

    void sendTransferNotification(String email, String senderName, String amount);
}