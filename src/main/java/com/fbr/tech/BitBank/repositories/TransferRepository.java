package com.fbr.tech.BitBank.repositories;

import com.fbr.tech.BitBank.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
