package com.fbr.tech.BitBank.controllers.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record DepositMoneyDto(
        @DecimalMin(value = "1.00", message = "O valor mínimo para depósito é R$ 1,00")
        BigDecimal value) {
}
