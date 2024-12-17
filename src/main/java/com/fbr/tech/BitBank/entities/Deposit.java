package com.fbr.tech.BitBank.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "deposit_tb")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Deposit {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "deposit_id")
    private UUID id;

    @Column(name = "deposit_value")
    private BigDecimal depositValue;

    @ManyToOne
    @JoinColumn(name = "wallet_receiver_id")
    private Wallet walletReceiver;

    @Column(name = "deposit_date_time")
    @CreationTimestamp
    private LocalDateTime depositDateTime;

    @Column(name = "ip_address")
    private String ipAddress;
}
