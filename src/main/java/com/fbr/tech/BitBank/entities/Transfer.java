package com.fbr.tech.BitBank.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfer_db")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transfer_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "wallet_receiver_id")
    private Wallet walletReceiver;

    @ManyToOne
    @JoinColumn(name = "wallet_sender_id")
    private Wallet walletSender;

    @Column(name = "transfer_value")
    private BigDecimal transferValue;

    @Column(name = "transfer_date_time")
    @CreationTimestamp
    private LocalDateTime transferDateTime;

}
