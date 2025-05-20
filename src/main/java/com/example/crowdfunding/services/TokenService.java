package com.example.crowdfunding.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crowdfunding.dto.AuthResponse;
import com.example.crowdfunding.exception.NoContentException;
import com.example.crowdfunding.models.Token;
import com.example.crowdfunding.models.Utilisateur;
import com.example.crowdfunding.repository.TokenRepository;
import com.example.crowdfunding.repository.UtilisateurRepository;
import com.example.crowdfunding.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

     
    private final JwtUtil jwtUtil;

    @Autowired
    private TokenRepository tokenRepository;

    
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    String pattern = "yyyy-MM-dd HH:mm";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(formatter);

    // Invalider les anciens tokens
    public void invalidateOldTokens(Utilisateur user) {
        List<Token> oldTokens = tokenRepository.findAllByUtilisateur(user);
        oldTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(oldTokens);
    }

    // Générer de nouveaux tokens pour un utilisateur
    public AuthResponse generateTokens(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new NoContentException("Utilisateur non trouvé"));

        // Invalider les anciens tokens
        invalidateOldTokens(user);

        // Générer un nouvel access token et refresh token
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        // Enregistrer le nouveau refresh token
        Token token = new Token();
        token.setToken(refreshToken);
        token.setIdToken(UUID.randomUUID().toString());
        token.setExpired(false);
        token.setRevoked(false);
        token.setDateAjout(formattedDateTime);
        token.setUtilisateur(user);
        tokenRepository.save(token);

        return new AuthResponse(accessToken, refreshToken);
    }

    // Réinitialiser les tokens et générer de nouveaux tokens
    public AuthResponse resetTokens(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Invalider les anciens tokens
        invalidateOldTokens(user);

        // Générer de nouveaux tokens
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        // Enregistrer le nouveau refresh token
        Token token = new Token();
        token.setToken(refreshToken);
        token.setIdToken(UUID.randomUUID().toString());
        token.setExpired(false);
        token.setRevoked(false);
        token.setDateModif(formattedDateTime);
        token.setUtilisateur(user);
        tokenRepository.save(token);

        return new AuthResponse(accessToken, refreshToken);
    }

}
