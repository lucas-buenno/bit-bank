package com.fbr.tech.BitBank.controllers.dto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferOperationDto(
                                   @DecimalMin( value = "0.01", message = "O valor mínimo para transferência é R$ 0,01")
                                   BigDecimal transferValue,
                                   UUID receiver
                                   ) {
}
