package com.orangemoney.api.dto.request;

import com.orangemoney.api.validation.annotation.ValidAmount;
import com.orangemoney.api.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class MerchantPaymentRequest {

    @NotBlank(message = "Le numéro du marchand est obligatoire")
    @ValidPhoneNumber
    private String merchantPhoneNumber;

    @NotNull(message = "Le montant est obligatoire")
    @ValidAmount(min = 100, max = 1000000)
    private BigDecimal amount;

    private String description;

    public String getMerchantPhoneNumber() {
        return merchantPhoneNumber;
    }

    public void setMerchantPhoneNumber(String merchantPhoneNumber) {
        this.merchantPhoneNumber = merchantPhoneNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}