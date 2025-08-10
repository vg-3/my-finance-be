package com.dev.my_finance.dto;

import lombok.Builder;

@Builder
public record RegisterRequest(String firstName,
                              String lastName,
                              String email,
                              String password,
                              boolean isAdmin) {
}
