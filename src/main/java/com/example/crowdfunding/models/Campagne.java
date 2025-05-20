package com.example.crowdfunding.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Campagne {

    @Id
    private String idCampagne;

    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int montantCible;

    private int montantActuel ;

    private int pourcentage;

    private String dateLimite;

    private String dateModif;

    private String imageUrl;

    private boolean validee = false;

    private boolean isActive = true;

    private String dateCreation ;

    // Relations
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur createur;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @OneToMany(mappedBy = "campagne", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contribution> contribution;

}
