package com.fbr.tech.BitBank.controllers.dto;

import java.util.List;

public record StatementDto(WalletStatementDto walletStatement,
                           List<StatementItemDto> statementItemDto,
                           PaginationResponseDto paginationResponse) {
}
