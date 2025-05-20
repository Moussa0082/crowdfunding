package com.example.crowdfunding.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crowdfunding.models.Contribution;
import com.example.crowdfunding.models.Utilisateur;

public interface ContributionRepository extends JpaRepository<Contribution, String> {

    List<Contribution> findAllByCampagneIdCampagne(String idCampagne);

    List<Contribution> findAllByContributeurIdUtilisateur(String idUtilisateur);

    Contribution findByidContribution(String idContribution);

    List<Contribution> findAllByContributeur_IdUtilisateur(String idContribution);

    List<Contribution> findAllByIdContributionOrderByDateContributionDesc(String idContribution);

    List<Contribution> findAllByIdContribution(String idContribution);

    List<Contribution> findAllByCampagne_IdCampagne(String idCampagne);


}
