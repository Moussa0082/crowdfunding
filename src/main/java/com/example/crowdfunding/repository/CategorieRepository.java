package com.example.crowdfunding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crowdfunding.models.Categorie;

public interface CategorieRepository  extends JpaRepository<Categorie, String>{

    Categorie findByNomCategorie(String nomCategorie);
    
}
