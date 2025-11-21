package com.orangemoney.api.dto.response;

import java.math.BigDecimal;

public class BalanceResponse {

    private BigDecimal balance;
    private String currency = "XOF";

    public BalanceResponse() {
    }

    public BalanceResponse(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}