package com.example.crowdfunding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crowdfunding.models.Token;
import com.example.crowdfunding.models.Utilisateur;

public interface TokenRepository extends JpaRepository<Token, String>{

    List<Token> findAllByUtilisateur(Utilisateur user);
    
}
