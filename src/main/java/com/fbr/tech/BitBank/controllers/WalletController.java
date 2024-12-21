package com.fbr.tech.BitBank.controllers;

import com.fbr.tech.BitBank.controllers.dto.*;
import com.fbr.tech.BitBank.entities.Deposit;
import com.fbr.tech.BitBank.services.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

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


    @GetMapping
    public ResponseEntity<ApiResponse<GetWalletsDto>> getWallets(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "walletCreationDate") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder
    ) {
        var response = walletService.getWallets(page, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(new ApiResponse<>(response.getContent(),
                new PaginationResponseDto(page, pageSize, response.getTotalElements(), response.getTotalPages())));
    }


    @DeleteMapping("/{walletId}")
    public ResponseEntity<Void> deleteWalletById(@PathVariable UUID walletId) {

        var isWalletDeleted = walletService.deleteWalletById(walletId);

        return isWalletDeleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/{walletId}/deposit")
    public ResponseEntity<Void> depositMoney(@PathVariable UUID walletId,
                                             @RequestBody @Valid DepositMoneyDto dto,
                                             HttpServletRequest request) {

        walletService.depositMoney(walletId, dto, request.getAttribute("x-user-ip"));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{walletId}/extrato")
    public ResponseEntity<StatementDto> getStatements(
            @PathVariable UUID walletId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "statement_date_time") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder
    ) {

        var statement = walletService.getStatement(walletId, page, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(statement);
    }
}




