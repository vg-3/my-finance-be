package com.dev.my_finance.service;

import com.dev.my_finance.config.JwtService;
import com.dev.my_finance.dto.AuthenticationRequest;
import com.dev.my_finance.dto.AuthenticationResponse;
import com.dev.my_finance.dto.RegisterRequest;
import com.dev.my_finance.dto.UserDto;
import com.dev.my_finance.entity.Token;
import com.dev.my_finance.entity.User;
import com.dev.my_finance.enumeration.Role;
import com.dev.my_finance.enumeration.TokenType;
import com.dev.my_finance.exceptions.ResourceNotFoundException;
import com.dev.my_finance.exceptions.UnauthorizedException;
import com.dev.my_finance.exceptions.UserAlreadyExistsException;
import com.dev.my_finance.repository.TokenRepository;
import com.dev.my_finance.repository.UserRepository;
import com.dev.my_finance.lib.HttpCookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${app.security.jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    
    private final TokenRepository tokenRepository;


    public AuthenticationResponse register(RegisterRequest request, HttpServletResponse response) {

        if(userRepository.existsByEmail(request.email())){
            throw new UserAlreadyExistsException("User Already Exists");
        }

        var user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.isAdmin() ? Role.ADMIN : Role.USER)
                .build();

        var savedUser =  userRepository.save(user);
        
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

//        revokeAllUserTokens(savedUser);

        saveUserToken(savedUser, jwtToken);

        HttpCookie.addHttpOnlyCookie(response, "refreshToken", refreshToken, "/", refreshTokenExpiration);

        UserDto userDto = UserDto.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .phoneNumber(savedUser.getPhoneNumber())
                .role(savedUser.getRole())
                .build();

            return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .user(userDto)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        var user = userRepository.findByEmail(request.email()).orElseThrow(
                ()-> new UsernameNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

//        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        HttpCookie.addHttpOnlyCookie(response, "refreshToken", refreshToken, "/", refreshTokenExpiration);

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .user(userDto)
                .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request,  HttpServletResponse response) {
        final String refreshToken = HttpCookie.getRefreshTokenFromCookies(request);
        final String userEmail;
        if(refreshToken == null){
            throw new UnauthorizedException("Missing refresh token");
        }
        userEmail =  jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        User userDetails = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var isTokenValid = tokenRepository.findByToken(refreshToken)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        if (!jwtService.isTokenValid(refreshToken, userDetails) && !isTokenValid) {
                throw new UnauthorizedException("Invalid refresh token");
        }

        var newAccessToken = jwtService.generateToken(userDetails);
        saveUserToken(userDetails, newAccessToken);

        UserDto userDto = UserDto.builder()
                .id(userDetails.getId())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email(userDetails.getEmail())
                .phoneNumber(userDetails.getPhoneNumber())
                .role(userDetails.getRole())
                .build();

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .user(userDto)
                .build();

    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user){
        var validTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validTokens.isEmpty()){
            return;
        }
        validTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }
}
