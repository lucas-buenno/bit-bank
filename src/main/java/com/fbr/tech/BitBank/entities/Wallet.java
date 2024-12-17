package com.fbr.tech.BitBank.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NotNull
@Data
@Table(name = "wallet_db")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "wallet_id")
    private UUID id;

    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "holder_name")
    private String holderName;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "wallet_creation_date")
    @CreationTimestamp
    private LocalDateTime walletCreationDate;


}
