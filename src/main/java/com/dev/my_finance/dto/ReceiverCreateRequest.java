package com.dev.my_finance.dto;

import lombok.Builder;

@Builder
public record ReceiverCreateRequest(String name,
                                    String contact,
                                    Long lenderId) {
}
