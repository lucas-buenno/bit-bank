package com.fbr.tech.BitBank.controllers;

import com.fbr.tech.BitBank.controllers.dto.TransferOperationDto;
import com.fbr.tech.BitBank.services.TransferService;
import com.fbr.tech.BitBank.services.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class TransferController {

    private final WalletService walletService;

    private final TransferService transferService;


    @PostMapping(path = "/{walletSenderId}/transfer")
    public ResponseEntity<Void> transferOperation(@PathVariable UUID walletSenderId,
                                                  @RequestBody @Valid TransferOperationDto dto,
                                                  HttpServletRequest request) {

        transferService.transferOperation(walletSenderId, dto, request.getAttribute("x-user-ip"));

        return ResponseEntity.ok().build();

    }


}
