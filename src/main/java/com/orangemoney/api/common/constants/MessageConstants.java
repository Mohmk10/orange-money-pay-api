package com.orangemoney.api.common.constants;

public final class MessageConstants {

    private MessageConstants() {
    }

    public static final String INSUFFICIENT_BALANCE = "Solde insuffisant pour effectuer cette transaction";
    public static final String INVALID_PIN = "Code PIN invalide";
    public static final String INVALID_PHONE_NUMBER = "Numéro de téléphone Orange invalide";
    public static final String ACCOUNT_BLOCKED = "Compte bloqué. Veuillez contacter le support";
    public static final String DAILY_LIMIT_EXCEEDED = "Limite journalière dépassée";
    public static final String TRANSACTION_NOT_FOUND = "Transaction introuvable";
    public static final String USER_NOT_FOUND = "Utilisateur introuvable";
    public static final String INVALID_OTP = "Code OTP invalide ou expiré";
    public static final String TRANSFER_SUCCESS = "Transfert effectué avec succès";
    public static final String KYC_REQUIRED = "Vérification d'identité requise pour ce montant";
}
