package com.fbr.tech.BitBank.controllers.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetWalletsDto (UUID id,
                            String cpf,
                            String email,
                            String holderName,
                            BigDecimal balance,
                            LocalDateTime walletCreationDate
                            ) {
}

