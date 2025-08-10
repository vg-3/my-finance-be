package com.dev.my_finance.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(int status, String message, LocalDateTime timestamp) {
}
