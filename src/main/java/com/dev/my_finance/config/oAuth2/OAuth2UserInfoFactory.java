package com.dev.my_finance.config.oAuth2;

import com.dev.my_finance.config.client.GithubFeignClient;
import com.dev.my_finance.dto.GithubEmailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2UserInfoFactory {

    private final GithubFeignClient githubFeignClient;

    public  OAuth2UserInfo getOAuth2UserInfo(String accessToken, String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase("GOOGLE")){
            return new GoogleOAuth2UserInfo(attributes);
        }else if(registrationId.equalsIgnoreCase("GITHUB")){

            String email = fetchGithubPrimaryEmail(accessToken);
            Map<String, Object> modifiableAttributes = new HashMap<>(attributes);
            if (email != null && !email.isEmpty()) {
                modifiableAttributes.put("email", email);
            } else {
                throw new OAuth2AuthenticationException("Could not retrieve primary email from GitHub.");
            }
            return new GithubOAuth2UserInfo(modifiableAttributes);
        }else {
            throw new OAuth2AuthenticationException("Login with" + registrationId+ " is not supported yet");
        }
    }

    private String fetchGithubPrimaryEmail(String accessToken) {
        List<GithubEmailResponse> responses =
                githubFeignClient.getUserEmails("token " + accessToken);
        return  responses.stream()
                .filter(r -> Boolean.TRUE.equals(r.primary()))
                .findFirst()
                .map(GithubEmailResponse::email)
                .orElse(null);
    }
}

