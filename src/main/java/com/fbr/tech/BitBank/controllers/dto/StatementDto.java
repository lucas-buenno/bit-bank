package com.fbr.tech.BitBank.controllers.dto;

import java.util.List;

public record StatementDto(
        WalletStatementDto walletStatement,
        List<StatementItemDto> statementItem,
        PaginationResponseDto paginationResponse
) {
}
