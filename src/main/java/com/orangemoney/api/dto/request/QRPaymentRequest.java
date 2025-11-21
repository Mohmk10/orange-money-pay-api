package com.orangemoney.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public class QRPaymentRequest {

    @NotBlank(message = "Le token QR est obligatoire")
    private String qrToken;

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }
}