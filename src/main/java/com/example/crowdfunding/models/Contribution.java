package com.example.crowdfunding.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data 
public class Contribution {

    @Id
    private String idContribution;

    private int montant;

    private String pieceJustificative;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String dateContribution ;

    private String dateModif ;



    // Relations
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur contributeur;

    @ManyToOne
    @JoinColumn(name = "campagne_id")
    private Campagne campagne;

}
