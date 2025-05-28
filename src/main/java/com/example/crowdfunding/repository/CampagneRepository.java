package com.example.crowdfunding.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crowdfunding.models.Campagne;
import com.example.crowdfunding.models.Utilisateur;

public interface CampagneRepository extends JpaRepository<Campagne, String>{

    Campagne findByTitreAndCreateur(String titre, Utilisateur createur);

    List<Campagne> findAllByCreateurIdUtilisateur(String idUtilisateur);

    List<Campagne> findAllByCategorieIdCategorie(String idCategorie);

    List<Campagne> findByValideeTrue();

    List<Campagne> findByValideeFalse();

    
}
