package com.fbr.tech.BitBank.services;

import com.fbr.tech.BitBank.controllers.dto.CreateWalletDto;
import com.fbr.tech.BitBank.controllers.dto.DepositMoneyDto;
import com.fbr.tech.BitBank.controllers.dto.GetWalletsDto;
import com.fbr.tech.BitBank.controllers.dto.TransferOperationDto;
import com.fbr.tech.BitBank.entities.Deposit;
import com.fbr.tech.BitBank.entities.Transfer;
import com.fbr.tech.BitBank.entities.Wallet;
import com.fbr.tech.BitBank.exception.DataAlreadyExistsException;
import com.fbr.tech.BitBank.exception.WalletBalanceException;
import com.fbr.tech.BitBank.exception.WalletNotFoundException;
import com.fbr.tech.BitBank.repositories.DepositRepository;
import com.fbr.tech.BitBank.repositories.TransferRepository;
import com.fbr.tech.BitBank.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletService {


    private WalletRepository walletRepository;

    private DepositRepository depositRepository;
    
    private TransferRepository transferRepository;


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


}
