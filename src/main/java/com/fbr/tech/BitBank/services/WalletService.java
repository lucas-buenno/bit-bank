package com.fbr.tech.BitBank.services;

import com.fbr.tech.BitBank.controllers.dto.CreateWalletDto;
import com.fbr.tech.BitBank.controllers.dto.GetWalletsDto;
import com.fbr.tech.BitBank.entities.Wallet;
import com.fbr.tech.BitBank.exception.DataAlreadyExistsException;
import com.fbr.tech.BitBank.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class WalletService {


    private WalletRepository walletRepository;


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
}
