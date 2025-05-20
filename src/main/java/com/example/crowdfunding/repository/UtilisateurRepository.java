package com.example.crowdfunding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crowdfunding.models.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String>{

    Utilisateur findByEmailOrNumero(String email, String numero);


    
}
