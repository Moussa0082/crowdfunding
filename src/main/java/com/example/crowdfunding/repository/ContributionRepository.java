package com.example.crowdfunding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crowdfunding.models.Contribution;

public interface ContributionRepository extends JpaRepository<Contribution, String> {
    
}
