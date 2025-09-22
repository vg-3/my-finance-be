package com.dev.my_finance.dto;

import lombok.Builder;

@Builder
public record LenderDto(Long id, String name) {}
