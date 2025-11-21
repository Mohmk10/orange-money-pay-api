package com.orangemoney.api.dto.request;

import com.orangemoney.api.validation.annotation.ValidAmount;
import com.orangemoney.api.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransferRequest {

    @NotNull(message = "Le montant est obligatoire")
    @ValidAmount(min = 100, max = 1000000, message = "Le montant doit être entre 100 et 1,000,000 FCFA")
    private BigDecimal amount;

    @NotBlank(message = "Le numéro du destinataire est obligatoire")
    @ValidPhoneNumber
    private String receiverPhoneNumber;

    private String description;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}