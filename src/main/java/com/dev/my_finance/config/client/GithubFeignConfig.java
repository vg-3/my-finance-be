package com.dev.my_finance.config.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GithubFeignConfig {

    @Bean
    public RequestInterceptor githubHeaders() {
        return template -> {
            template.header("Accept", "application/vnd.github+json");
            template.header("User-Agent", "Spring-Boot-App");
        };
    }
}

