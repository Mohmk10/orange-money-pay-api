package com.orangemoney.api.dto.request;

import com.orangemoney.api.validation.annotation.ValidAmount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BankTransferRequest {

    @NotNull(message = "L'ID du compte bancaire est obligatoire")
    private Long linkedBankAccountId;

    @NotBlank(message = "La direction est obligatoire (TO_BANK ou FROM_BANK)")
    private String direction;

    @NotNull(message = "Le montant est obligatoire")
    @ValidAmount(min = 1000, max = 5000000)
    private BigDecimal amount;

    public Long getLinkedBankAccountId() {
        return linkedBankAccountId;
    }

    public void setLinkedBankAccountId(Long linkedBankAccountId) {
        this.linkedBankAccountId = linkedBankAccountId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}