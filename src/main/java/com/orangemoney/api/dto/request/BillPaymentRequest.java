package com.orangemoney.api.dto.request;

import com.orangemoney.api.validation.annotation.ValidAmount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BillPaymentRequest {

    @NotBlank(message = "La catégorie est obligatoire")
    private String category;

    @NotBlank(message = "Le fournisseur est obligatoire")
    private String provider;

    @NotBlank(message = "Le numéro de compte est obligatoire")
    private String accountNumber;

    @NotNull(message = "Le montant est obligatoire")
    @ValidAmount(min = 100, max = 1000000)
    private BigDecimal amount;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}