package com.dev.my_finance.dto;

public record GithubEmailResponse(String email,
                                  Boolean primary,
                                  Boolean verified,
                                  String Visibility) {
}
