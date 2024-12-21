package com.fbr.tech.BitBank.controllers.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WalletStatementDto(UUID walletId,
                                 String cpf,
                                 String email,
                                 BigDecimal balance,
                                 LocalDateTime walletCreationDate
                                 ) {
}
