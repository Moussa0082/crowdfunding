package com.example.crowdfunding.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Contact {
    
    @Id
    private String  idContact;

    @Column(nullable = false)
    private String nomComplet;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telephone;
  
    @Column(nullable = false)
    private String dateAjout;

    @Column(columnDefinition = "TEXT")
    private String message;
}