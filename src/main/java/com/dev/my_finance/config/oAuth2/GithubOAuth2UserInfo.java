package com.dev.my_finance.config.oAuth2;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getFirstName() {
        String name = (String) attributes.get("name");
        if (name == null) return null;
        return name.split(" ")[0];
    }

    @Override
    public String getLastName() {
        String name = (String) attributes.get("name");
        if (name == null) return null;
        String[] parts = name.split(" ");
        return parts.length > 1 ? parts[1] : null;
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}
