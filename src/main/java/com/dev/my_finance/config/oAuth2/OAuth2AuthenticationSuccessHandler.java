package com.dev.my_finance.config.oAuth2;

import com.dev.my_finance.config.JwtService;
import com.dev.my_finance.entity.User;
import com.dev.my_finance.lib.HttpCookie;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.security.jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        User user = oauthUser.getUser();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        HttpCookie.addHttpOnlyCookie(response, "refreshToken", refreshToken, "/", refreshTokenExpiration);

        String frontendCallbackUrl = "http://localhost:3000/oauth/callback";
        String finalTarget = UriComponentsBuilder.fromUriString(frontendCallbackUrl)
                .queryParam("accessToken", jwtToken)
                .queryParam("userId", user.getId())
                .queryParam("firstName", user.getFirstName())
                .queryParam("lastName", user.getLastName())
                .queryParam("email", user.getEmail())
                .queryParam("phoneNumber", user.getPhoneNumber())
                .queryParam("role", user.getRole())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, finalTarget);
    }
}