package com.example.crowdfunding.models;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private BigInteger montantCible;

    private BigInteger montantActuel ;

    private double pourcentage;

    // @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateLimite;


    private String jourRestant;

    private String lieu;

    private String dateModif;

    private String imageUrl;

    private boolean validee = false;

    private boolean isActive = true;

    @Column(name = "date_creation")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateCreation;

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
