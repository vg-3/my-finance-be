package com.dev.my_finance.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String accessToken, UserDto user) {
}
