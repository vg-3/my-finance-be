package com.dev.my_finance.dto;

import com.dev.my_finance.enumeration.Role;
import lombok.Builder;

@Builder
public record UserDto(
         Long id,
         String firstName,
         String lastName,
         String email,
         String phoneNumber,
         Role role
) {
}
