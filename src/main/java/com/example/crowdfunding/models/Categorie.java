package com.example.crowdfunding.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Categorie {
    
    @Id
    private String idCategorie;

    @Column(nullable = false)
    private String nomCategorie;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = true)
    private String image;


    private String dateCreation;


    private String dateModif;
    
    @OneToMany(mappedBy = "categorie")
    @JsonIgnore
    private List<Campagne> campagnes;

}
