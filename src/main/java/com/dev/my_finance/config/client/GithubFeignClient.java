package com.dev.my_finance.config.client;

import com.dev.my_finance.dto.GithubEmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "githubClient",
        url = "https://api.github.com",
        configuration = GithubFeignConfig.class
)
public interface GithubFeignClient {

    @GetMapping("/user/emails")
    List<GithubEmailResponse> getUserEmails(
            @RequestHeader("Authorization") String token
    );
}

