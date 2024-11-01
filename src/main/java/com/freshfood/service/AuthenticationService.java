package com.freshfood.service;


import com.freshfood.dto.request.SignInRequest;
import com.freshfood.dto.response.TokenResponse;
import com.freshfood.exception.InvalidDataException;
import com.freshfood.model.Token;
import com.freshfood.model.User;
import com.freshfood.repository.CartRepository;
import com.freshfood.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.freshfood.util.TokenType.ACCESS_TOKEN;
import static com.freshfood.util.TokenType.REFRESH_TOKEN;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse authenticate(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        var user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Username or password is incorrect"));
        var cart = cartRepository.findByUser(user);
        System.out.println(user);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        tokenService.save(Token.builder().username(user.getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(signInRequest.getUsername())
                .userId( user.getId())
                .cartId(cart.get().getId())
                .build();
    }
    public TokenResponse refreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("x-token");
        if(StringUtils.isBlank(refreshToken )){
            throw new InvalidDataException("Token must be not blank");
        }
        final String username = jwtService.extractUsername(refreshToken , REFRESH_TOKEN);

        Optional<User> user = userRepository.findByUsername(username);
        if(!jwtService.isValid(refreshToken , REFRESH_TOKEN,user.get())){
            throw new InvalidDataException("Invalid token");
        }
        String accessToken = jwtService.generateToken(user.get());

        tokenService.save(Token.builder().username(user.get().getUsername()).accessToken(accessToken).refreshToken(refreshToken).build());


        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId((user.get().getId()))
                .username(user.get().getUsername())
                .build();
    }
    public String logout(HttpServletRequest request){
        String token = request.getHeader("x-token");
        if(StringUtils.isBlank(token)){
            throw new InvalidDataException("Token must be not blank");
        }
        final String username = jwtService.extractUsername(token, ACCESS_TOKEN);
        tokenService.delete(username);
        return "deleted";
    }

}
