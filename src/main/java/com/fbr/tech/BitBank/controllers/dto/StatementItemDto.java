package com.fbr.tech.BitBank.controllers.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StatementItemDto(
        String statement_id,
        String type,
        LocalDateTime statementDateTime,
        OperationCategory category,
        BigDecimal statementValue,
        String statementIpAddress,
        String walletReceiverId,
        String walletSenderId
) {
}
