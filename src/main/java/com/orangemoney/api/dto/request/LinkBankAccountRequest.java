package com.orangemoney.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LinkBankAccountRequest {

    @NotBlank(message = "Le nom de la banque est obligatoire")
    private String bankName;

    @NotBlank(message = "Le numéro de compte est obligatoire")
    private String accountNumber;

    @NotBlank(message = "Le nom du titulaire est obligatoire")
    private String accountHolderName;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }
}