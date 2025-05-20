package com.example.crowdfunding.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Utilisateur {

    @Id
    private String idUtilisateur;

    private String prenom;

    private String nom;

    private String numero;

    private String nonStructure;
    
    private String addresse; 

    private String photo; 

    @Column(unique = true, nullable = true)
    private String email;


    private String refreshToken;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleUtilisateur roleUtilisateur;

    private String dateModif;

    private boolean actif = true;

    private String dateInscription;

    // Relations
    @OneToMany(mappedBy = "createur", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Campagne> campagnes;

    @OneToMany(mappedBy = "contributeur", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contribution> contribution;

}
