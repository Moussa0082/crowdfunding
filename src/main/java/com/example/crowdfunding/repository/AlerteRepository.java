package com.example.crowdfunding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crowdfunding.models.Alerte;

public interface AlerteRepository  extends JpaRepository<Alerte, String>{
    
}
