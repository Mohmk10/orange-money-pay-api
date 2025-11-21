package com.orangemoney.api.dto.request;

import com.orangemoney.api.validation.annotation.ValidAmount;
import com.orangemoney.api.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RechargeRequest {

    @NotBlank(message = "Le type de recharge est obligatoire")
    private String type;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @ValidPhoneNumber
    private String phoneNumber;

    @NotNull(message = "Le montant est obligatoire")
    @ValidAmount(min = 100, max = 100000)
    private BigDecimal amount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}