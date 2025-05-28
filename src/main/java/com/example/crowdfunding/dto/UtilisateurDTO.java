package com.example.crowdfunding.dto;


import com.example.crowdfunding.models.RoleUtilisateur;
import com.example.crowdfunding.models.Utilisateur;

import lombok.Data;

@Data
public class UtilisateurDTO {
    private String idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private RoleUtilisateur roleUtilisateur;
    private boolean actif;

    public UtilisateurDTO(Utilisateur utilisateur) {
        this.idUtilisateur = utilisateur.getIdUtilisateur();
        this.nom = utilisateur.getNom();
        this.prenom = utilisateur.getPrenom();
        this.password = utilisateur.getPassword();
        this.roleUtilisateur = utilisateur.getRoleUtilisateur();
        this.email = utilisateur.getEmail();
        this.actif = utilisateur.isActif();
    }

    // Getters et setters
}
