package com.example.crowdfunding.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private UtilisateurDTO utilisateur;

    public LoginResponse(String accessToken, String refreshToken, UtilisateurDTO utilisateur) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.utilisateur = utilisateur;
    }

    // Getters et setters
}
