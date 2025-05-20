package com.example.crowdfunding.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Alerte {
    
    @Id
    private String idAlerte;

    @Column(nullable = true)
    private String sujet;

    @Column(nullable = true)
    private String email;

    @Column(length = 2000, nullable = false)
    private String message;

    private String dateAjout;

 
    // Constructeur par défaut
    public Alerte() {
    }

    public Alerte(String email, String message, String sujet){
     this.email = email;
     this.message = message;
     this.sujet = sujet;
    }

   

    // public Alerte(String email, String message, String sujet, Acteur acteur){
    //   this.email = email;
    //   this.message = message;
    //   this.sujet = sujet;
    //   this.acteur = acteur;
    // }

  public Alerte(String email, String message){

    this.email = email;
    this.message = message;

  }



}
