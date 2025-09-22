package com.dev.my_finance.dto;

import lombok.Builder;

@Builder
public record ReceiverDto(Long id,
                          String name,
                          String contact) {}
