package com.dev.my_finance.dto;

import lombok.Builder;

@Builder
public record AuthenticationRequest(String email, String password) {
}
