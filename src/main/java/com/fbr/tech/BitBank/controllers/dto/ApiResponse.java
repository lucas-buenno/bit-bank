package com.fbr.tech.BitBank.controllers.dto;

import java.util.List;

public record ApiResponse<T> (List<T> content,
                              PaginationResponseDto paginationResponseDto) {
}
