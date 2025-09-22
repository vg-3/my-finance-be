package com.dev.my_finance.config;

import com.dev.my_finance.exceptions.UnauthorizedException;
import com.dev.my_finance.repository.TokenRepository;
import com.dev.my_finance.utils.HttpCookie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new UnauthorizedException("Unauthorized");
        }
        jwt = authHeader.substring(7);


        var storedToken = tokenRepository.findByToken(jwt).orElse(null);
        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            HttpCookie.clearHttpOnlyCookie(response, "refreshToken", "/");
        }

    }
}
