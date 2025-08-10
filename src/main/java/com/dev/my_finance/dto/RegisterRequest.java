package com.dev.my_finance.dto;

import com.dev.my_finance.enumeration.Role;
import lombok.Builder;

@Builder
public record RegisterRequest(String firstName,
                              String lastName,
                              String email,
                              String password,
                              boolean isAdmin) {
}
