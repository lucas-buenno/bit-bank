package com.fbr.tech.BitBank.controllers;

import com.fbr.tech.BitBank.controllers.dto.ApiResponse;
import com.fbr.tech.BitBank.controllers.dto.CreateWalletDto;
import com.fbr.tech.BitBank.controllers.dto.GetWalletsDto;
import com.fbr.tech.BitBank.controllers.dto.PaginationResponseDto;
import com.fbr.tech.BitBank.services.WalletService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping
    ResponseEntity<ApiResponse<GetWalletsDto>> getWallets(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "walletCreationDate") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder
    ) {
        var response = walletService.getWallets(page, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(new ApiResponse<>(response.getContent(),
                new PaginationResponseDto(page, pageSize, response.getTotalElements(), response.getTotalPages())));
    }
    }


