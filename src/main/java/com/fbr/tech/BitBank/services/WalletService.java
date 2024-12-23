package com.fbr.tech.BitBank.services;

import com.fbr.tech.BitBank.controllers.dto.*;
import com.fbr.tech.BitBank.entities.Deposit;
import com.fbr.tech.BitBank.entities.Wallet;
import com.fbr.tech.BitBank.exception.DataAlreadyExistsException;
import com.fbr.tech.BitBank.exception.StatementOperationTypeException;
import com.fbr.tech.BitBank.exception.WalletBalanceException;
import com.fbr.tech.BitBank.exception.WalletNotFoundException;
import com.fbr.tech.BitBank.repositories.DepositRepository;
import com.fbr.tech.BitBank.repositories.TransferRepository;
import com.fbr.tech.BitBank.repositories.WalletRepository;
import com.fbr.tech.BitBank.repositories.dto.StatementView;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletService {


    private WalletRepository walletRepository;

    private DepositRepository depositRepository;


    public Wallet createWallet(CreateWalletDto dto) {

        var wallet = walletRepository.findWalletByCpfOrEmail(dto.cpf(), dto.email());

        if (wallet.isPresent()) {
            throw new DataAlreadyExistsException("Já existe um titular cadastrado com este CPF ou e-mail");
        }

        Wallet createdWallet = toWalletEntity(dto);

        return walletRepository.save(createdWallet);

    }

    private static Wallet toWalletEntity(CreateWalletDto dto) {
        Wallet createdWallet = new Wallet();

        createdWallet.setCpf(dto.cpf());
        createdWallet.setEmail(dto.email());
        createdWallet.setHolderName(dto.holderName());
        createdWallet.setBalance(BigDecimal.ZERO);
        return createdWallet;
    }


    public Page<GetWalletsDto> getWallets(Integer page, Integer pageSize, String sortBy, String sortOrder) {

        var direction = Sort.Direction.DESC;
        if (!sortOrder.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.ASC;
        }

        var pageRequest = PageRequest.of(page, pageSize, direction, sortBy);

        return walletRepository.findAll(pageRequest)
                .map(wallet -> new GetWalletsDto(
                        wallet.getId(),
                        wallet.getCpf(),
                        wallet.getEmail(),
                        wallet.getHolderName(),
                        wallet.getBalance(),
                        wallet.getWalletCreationDate()
                ));
    }

    @Transactional
    public boolean deleteWalletById(UUID walletID) {

        var wallet = walletRepository.findById(walletID);

        if (wallet.isPresent() && wallet.get().getBalance().compareTo(BigDecimal.ZERO ) > 0) {
            throw new WalletBalanceException("A carteira possui saldo disponível");
        }

        walletRepository.deleteById(walletID);

        return wallet.isPresent();

    }


    @Transactional
    public void depositMoney(UUID walletId, @Valid DepositMoneyDto dto, Object attribute) {

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Não existe carteira com o ID informado."));

        var deposit = new Deposit();
        deposit.setDepositValue(dto.value());
        deposit.setWalletReceiver(wallet);
        deposit.setIpAddress(attribute.toString());
        depositRepository.save(deposit);


        wallet.setBalance(wallet.getBalance().add(dto.value()));
        walletRepository.save(wallet);
    }

    private Wallet findWallet(UUID walletId) {
        
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Não encontramos nenhuma carteira com este ID"));
    }

    public StatementDto getStatement(UUID walletId, Integer page, Integer pageSize, String sortBy, String sortOrder) {

        var wallet = findWallet(walletId);
        var direction = Sort.Direction.DESC;

        if (!sortOrder.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.ASC;
        }

        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);

       var statement = walletRepository.getStatement(walletId.toString(), pageRequest)
                .map(statementView -> mapToDto(walletId, statementView));

       return new StatementDto(
               new WalletStatementDto(
                       wallet.getId(),
                       wallet.getCpf(),
                       wallet.getEmail(),
                       wallet.getHolderName(),
                       wallet.getBalance(),
                       wallet.getWalletCreationDate()
                    ),
                       statement.getContent(),
                       new PaginationResponseDto(
                               page,
                               pageSize,
                               statement.getTotalElements(),
                               statement.getTotalPages()
                       )
               );
    }

    private StatementItemDto mapToDto(UUID walletId, StatementView statementView) {

        if (statementView.getType().equalsIgnoreCase("deposit")) {
            return mapToDeposit(statementView);
        }

        if (statementView.getType().equalsIgnoreCase("transfer")
                && walletId.toString().equalsIgnoreCase(statementView.getWalletSenderId())) {

            return mapWhenSentTransfer(statementView);
        }

        if (statementView.getType().equalsIgnoreCase("transfer")
                && walletId.toString().equalsIgnoreCase(statementView.getWalletReceiverId())) {

            return mapWhenReceivedTransfer(statementView);
        }

        throw new StatementOperationTypeException("O tipo de operação informado é inválido");
    }

    private StatementItemDto mapWhenSentTransfer(StatementView statementView) {

        return new StatementItemDto(
                statementView.getStatementId(),
                statementView.getType(),
                OperationCategory.DEBIT,
                statementView.getStatementDateTime(),
                statementView.getStatementValue(),
                statementView.getIpAddress(),
                statementView.getWalletReceiverId(),
                statementView.getWalletSenderId()
        );
    }

    private StatementItemDto mapWhenReceivedTransfer(StatementView statementView) {

        return new StatementItemDto(
                statementView.getStatementId(),
                statementView.getType(),
                OperationCategory.CREDIT,
                statementView.getStatementDateTime(),
                statementView.getStatementValue(),
                statementView.getIpAddress(),
                statementView.getWalletReceiverId(),
                statementView.getWalletSenderId()
        );
    }

    private StatementItemDto mapToDeposit(StatementView statementView) {
        return new StatementItemDto(
                statementView.getStatementId(),
                statementView.getType(),
                OperationCategory.CREDIT,
                statementView.getStatementDateTime(),
                statementView.getStatementValue(),
                statementView.getIpAddress(),
                statementView.getWalletSenderId(),
                statementView.getWalletReceiverId()
        );
    }
}
