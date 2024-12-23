package com.fbr.tech.BitBank.repositories.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface StatementView {

    String getStatementId();
    String getType();
    LocalDateTime getStatementDateTime();
    BigDecimal getStatementValue();
    String getIpAddress();
    String getWalletSenderId();
    String getWalletReceiverId();
}
