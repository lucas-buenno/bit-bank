package com.fbr.tech.BitBank.repositories;

import com.fbr.tech.BitBank.entities.Wallet;
import com.fbr.tech.BitBank.repositories.dto.StatementView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {


    String SQL_STATEMENT =  """
    SELECT
        BIN_TO_UUID(deposit_id) as statement_id,
        "deposit" as type,
        deposit_date_time as statement_date_time,
        deposit_value as statement_value,
        ip_address as statement_ip_address,
        BIN_TO_UUID(wallet_receiver_id) as wallet_receiver_id,
        "" as wallet_sender_id
    FROM deposit_tb
    WHERE wallet_receiver_id = UUID_TO_BIN(:teste123)
    UNION ALL
    SELECT
        BIN_TO_UUID(transfer_id) as statement_id,
        "transfer" as type,
        transfer_date_time as statement_date_time,
        transfer_value as statement_value,
        ip_address as statement_ip_address,
        BIN_TO_UUID(wallet_receiver_id) as wallet_receiver_id,
        BIN_TO_UUID(wallet_sender_id) as wallet_sender_id
    FROM transfer_db
    WHERE wallet_receiver_id = UUID_TO_BIN(:teste123) OR wallet_sender_id = UUID_TO_BIN(:walletId)
""";


    String SQL_COUNT_STATEMENT = """
             SELECT (*) FROM (
            """ + SQL_STATEMENT + """
             ) as total
            """;

    Optional<Wallet> findWalletByCpfOrEmail(String cpf, String email);

    @Query(value = SQL_STATEMENT, countQuery = SQL_COUNT_STATEMENT, nativeQuery = true)
    Page<StatementView> getStatement(String teste123, PageRequest pageRequest);

}
