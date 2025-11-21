package com.orangemoney.api.dto.projection;

import java.math.BigDecimal;

public interface AccountBalanceProjection {

    String getAccountNumber();

    BigDecimal getBalance();

    BigDecimal getDailyLimit();

    String getStatus();
}