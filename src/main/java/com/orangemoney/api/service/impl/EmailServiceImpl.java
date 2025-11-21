package com.orangemoney.api.service.impl;

import com.orangemoney.api.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendVerificationEmail(String email, String token) {
        String verificationLink = "http://localhost:8080/api/v1/auth/verify?token=" + token;

        logger.info("=== EMAIL DE VÉRIFICATION ===");
        logger.info("À : {}", email);
        logger.info("Sujet : Vérifiez votre compte Orange Money");
        logger.info("Lien de vérification : {}", verificationLink);
        logger.info("==============================");
    }

    @Override
    public void sendTransferNotification(String email, String senderName, String amount) {
        logger.info("=== NOTIFICATION TRANSFERT ===");
        logger.info("À : {}", email);
        logger.info("Sujet : Vous avez reçu {} FCFA de {}", amount, senderName);
        logger.info("==============================");
    }
}