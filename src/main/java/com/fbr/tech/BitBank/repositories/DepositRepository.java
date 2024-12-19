package com.fbr.tech.BitBank.repositories;

import com.fbr.tech.BitBank.entities.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepositRepository extends JpaRepository<Deposit, UUID> {
}
