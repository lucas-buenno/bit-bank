package com.fbr.tech.BitBank.controllers.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

public record CreateWalletDto(@CPF(message = "Por favor, informe um CPF válido")
                              @NotBlank(message = "O CPF não pode ficar em branco") String cpf,
                              @Email(message = "Por favor, informe um e-mail válido")
                              @NotBlank(message = "O e-mail não pode ficar em branco") String email,
                              @NotBlank(message = "Por favor, informe um nome válido")
                              @Length(min = 2, message = "Por favor, informe um nome válido") String holderName) {
}
