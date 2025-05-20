package com.example.crowdfunding.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Token {

    @Id
    private String idToken;

    private String token; // le refreshToken


    private String dateAjout; // le refreshToken

    private boolean expired;
    private boolean revoked;

    @ManyToOne
    private Utilisateur utilisateur;

}
