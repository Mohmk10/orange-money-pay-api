package com.orangemoney.api.controller;

import com.orangemoney.api.exception.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestExceptionController {

    @GetMapping("/exception/{type}")
    public String testException(@PathVariable String type) {
        switch (type) {
            case "notfound":
                throw new ResourceNotFoundException("Utilisateur introuvable");
            case "balance":
                throw new InsufficientBalanceException("Solde insuffisant");
            case "pin":
                throw new InvalidPinException("Code PIN invalide");
            case "blocked":
                throw new AccountBlockedException("Compte bloqué");
            case "limit":
                throw new DailyLimitExceededException("Limite journalière dépassée");
            case "otp":
                throw new InvalidOtpException("Code OTP invalide");
            case "kyc":
                throw new KycRequiredException("Vérification KYC requise");
            default:
                return "Test OK";
        }
    }
}
