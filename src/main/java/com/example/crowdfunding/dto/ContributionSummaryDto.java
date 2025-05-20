package com.example.crowdfunding.dto;

import java.time.LocalDateTime;

import com.example.crowdfunding.models.Utilisateur;

import lombok.Data;

@Data
public class ContributionSummaryDto {
    private Utilisateur utilisateur;
    private int montantTotal;
    private LocalDateTime derniereDate;

    public ContributionSummaryDto(Utilisateur utilisateur, int montantTotal, LocalDateTime derniereDate) {
    this.utilisateur = utilisateur;
    this.montantTotal = montantTotal;
    this.derniereDate = derniereDate;
}


}
