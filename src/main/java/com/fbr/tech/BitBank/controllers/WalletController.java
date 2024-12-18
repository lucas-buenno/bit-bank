package com.fbr.tech.BitBank.controllers;

import com.fbr.tech.BitBank.controllers.dto.CreateWalletDto;
import com.fbr.tech.BitBank.services.WalletService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(path = "/wallets")
@AllArgsConstructor
public class WalletController {


    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody @Valid CreateWalletDto dto) {

        var wallet = walletService.createWallet(dto);

        return ResponseEntity.created(URI.create("/" + wallet.getId())).build();
    }

}
