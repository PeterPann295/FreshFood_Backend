package com.freshfood.service;


import com.freshfood.exception.ResourceNotFoundException;
import com.freshfood.model.Token;
import com.freshfood.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record TokenService(TokenRepository tokenRepository) {
    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    public int save(Token token) {
        Optional<Token> existingToken = tokenRepository.findByUsername(token.getUsername());
        if (!existingToken.isPresent()) {
            tokenRepository.save(token);
            return token.getId();
        }else {
            Token t = existingToken.get();
            t.setAccessToken(token.getAccessToken());
            t.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(t);
            return t.getId();

        }
    }
    public void delete(String username){
        Token token = tokenRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        tokenRepository.delete(token);
    }
}
