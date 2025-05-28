package com.example.crowdfunding.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;

import com.example.crowdfunding.models.Utilisateur;

import lombok.Data;

@Data
public class ContributionSummaryDto {
    private Utilisateur utilisateur;
    private BigInteger montantTotal;
    private LocalDateTime derniereDate;

    public ContributionSummaryDto(Utilisateur utilisateur, BigInteger montantTotal, LocalDateTime derniereDate) {
    this.utilisateur = utilisateur;
    this.montantTotal = montantTotal;
    this.derniereDate = derniereDate;
}


}
