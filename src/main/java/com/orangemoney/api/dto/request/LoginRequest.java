package com.orangemoney.api.dto.request;

import com.orangemoney.api.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @ValidPhoneNumber
    private String phoneNumber;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}