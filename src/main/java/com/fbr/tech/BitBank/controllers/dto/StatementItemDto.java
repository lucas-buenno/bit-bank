package com.fbr.tech.BitBank.controllers.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public record StatementItemDto(
        String statement_id,
        String type,
        OperationCategory category,
        LocalDateTime statement_date_time,
        BigDecimal value,
        String ipAddress,
        String wallet_receiver_id,
        String wallet_sender_id
) {
}
