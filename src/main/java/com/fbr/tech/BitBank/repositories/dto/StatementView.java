package com.fbr.tech.BitBank.repositories.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StatementView {

    String getStatementId();
    String getType();
    LocalDateTime getStatementDateTime();
    BigDecimal getStatementValue();
    String getStatementIpAddress();
    String getWalletReceiverId();
    String getWalletSenderId();
}
