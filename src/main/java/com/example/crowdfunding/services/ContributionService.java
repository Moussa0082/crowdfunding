package com.example.crowdfunding.services;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.crowdfunding.dto.ContributionSummaryDto;
import com.example.crowdfunding.exception.InvalidAttributeException;
import com.example.crowdfunding.exception.NoContentException;
import com.example.crowdfunding.models.Campagne;
import com.example.crowdfunding.models.Contribution;
import com.example.crowdfunding.models.Utilisateur;
import com.example.crowdfunding.repository.CampagneRepository;
import com.example.crowdfunding.repository.ContributionRepository;
import com.example.crowdfunding.repository.UtilisateurRepository;


@Service
public class ContributionService {

    @Autowired
    ContributionRepository contributionRepository;


    @Autowired
    CampagneRepository campagneRepository;
    
    @Autowired
    UtilisateurRepository utilisateurRepository;

     @Autowired
    ImageService imageService;


    String pattern = "yyyy-MM-dd HH:mm";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(formatter);

    public Contribution ajouterContribution(Contribution contribution, String idCampagne, MultipartFile photo) throws IOException {
        // Récupérer la campagne liée à cette contribution
        Campagne campagne = campagneRepository.findById(idCampagne)
            .orElseThrow(() -> new NoContentException("Campagne non trouvée pour cette contribution"));
    
        contribution.setIdContribution(UUID.randomUUID().toString());
    
        if (photo != null && !photo.isEmpty()) {
            String imageBase64 = imageService.convertirEtSauvegarderEnWebp(photo, contribution.getIdContribution());
            contribution.setPieceJustificative(imageBase64); 
        }
    
        contribution.setDateContribution(formattedDateTime); // Assure-toi que formattedDateTime est bien défini
    
        // Associer la campagne à la contribution
        contribution.setCampagne(campagne);
    
        // Mettre à jour le montant actuel et le pourcentage de la campagne
        BigInteger nouveauMontant = campagne.getMontantActuel().add(contribution.getMontant());
        campagne.setMontantActuel(nouveauMontant);
    
        BigInteger cent = BigInteger.valueOf(100);
        BigInteger produit = nouveauMontant.multiply(cent);
        BigInteger montantCible = campagne.getMontantCible();
        
        // Diviser en double pour avoir un pourcentage réel (si tu veux un pourcentage entier arrondi)
        double pourcentage = produit.doubleValue() / montantCible.doubleValue();
        campagne.setPourcentage(pourcentage);
    
        // Sauvegarder d’abord la campagne mise à jour
        campagneRepository.save(campagne);
    
        // Puis sauvegarder la contribution
        return contributionRepository.save(contribution);
    }    


    public Contribution modifierContribution(Contribution contribution, String idContribution, MultipartFile photo) throws IOException {

        Contribution c = contributionRepository.findById(idContribution)
            .orElseThrow(() -> new NoContentException("Contribution non trouvée"));
    
        Campagne campagne = c.getCampagne();
        if (campagne == null) {
            throw new NoContentException("Aucune campagne associée à cette contribution");
        }
    
        if (photo != null && !photo.isEmpty()) {
            String imageBase64 = imageService.convertirEtSauvegarderEnWebp(photo, c.getIdContribution());
            c.setPieceJustificative(imageBase64); 
        }
    
        if (contribution.getMontant().compareTo(BigInteger.ZERO) <= 0) {
            throw new InvalidAttributeException("Le montant de la contribution doit être supérieur à 0");
        }
        
    
        // Recalculer le montant de la campagne
        BigInteger ancienMontant = c.getMontant();
        BigInteger nouveauMontant = contribution.getMontant();
    
        c.setMontant(nouveauMontant);
        c.setDescription(contribution.getDescription());
        c.setDateModif(formattedDateTime); // Assure-toi que cette variable est bien une String formatée
    
        // Mettre à jour la campagne
        BigInteger montantActuelCorrige = campagne.getMontantActuel().subtract(ancienMontant).add(nouveauMontant) ;
        campagne.setMontantActuel(montantActuelCorrige);
    
        BigInteger cent = BigInteger.valueOf(100);
        BigInteger produit = montantActuelCorrige.multiply(cent);
        BigInteger montantCible = campagne.getMontantCible();
        
        // Diviser en double pour avoir un pourcentage réel (si tu veux un pourcentage entier arrondi)
        double pourcentage = produit.doubleValue() / montantCible.doubleValue();

        // int pourcentage = (int) ((montantActuelCorrige * 100.0) / campagne.getMontantCible());
        campagne.setPourcentage(pourcentage);
    
        campagneRepository.save(campagne);
        return contributionRepository.save(c);
    }
    


    public List<Contribution> getAllContribution() {
        List<Contribution> contributions = contributionRepository.findAll();
    
        if (contributions.isEmpty())
            throw new NoContentException("Aucune contribution trouvée");

        contributions.sort(Comparator.comparing(Contribution::getDateContribution).reversed());
        
        return contributions;
    }

    public List<Contribution> getAllContributionByContributeur(String idUtilisateur) {
        List<Contribution> contributions = contributionRepository.findAllByContributeurIdUtilisateur(idUtilisateur);
    
        if (contributions.isEmpty())
            throw new NoContentException("Aucune campagne trouvée");

        contributions.sort(Comparator.comparing(Contribution::getDateContribution).reversed());
        
        return contributions;
    }

    public List<Contribution> getAllContributionByCampagne(String idContribution) {
        List<Contribution> contributions = contributionRepository.findAllByCampagneIdCampagne(idContribution);
    
        if (contributions.isEmpty())
            throw new NoContentException("Aucune contribution trouvée");

        contributions.sort(Comparator.comparing(Contribution::getDateContribution).reversed());
        
        return contributions;
    }


    // public List<Utilisateur> getAllUtilisateurByContribution(String idContribution) {
    //     // Contribution contribution = contributionRepository.findById(idContribution).orElseThrow(() -> new NoContentException("Contribution non trouvée") );
    //     List<Utilisateur> utilisateurs = contributionRepository.findAllByContributeur_IdUtilisateur(idContribution);
    
    //     if (utilisateurs.isEmpty())
    //         throw new NoContentException("Aucun utilisateur trouvé pour cette contribution");

    //     // utilisateurs.sort(Comparator.comparing(Contribution::getDateContribution).reversed());
        
    //     return utilisateurs;
    // }

    // public List<Utilisateur> getAllUtilisateurByContribution(String idContribution) {
    // List<Contribution> contributions = contributionRepository.findAllByIdContributionOrderByDateContributionDesc(idContribution);

    // if (contributions.isEmpty())
    //     throw new NoContentException("Aucune contribution trouvée");

    // return contributions.stream()
    //         .map(Contribution::getContributeur)
    //         .collect(Collectors.toList());
    // }

    public List<ContributionSummaryDto> getContributionSummaryByCampagne(String idCampagne) {
        List<Contribution> contributions = contributionRepository.findAllByCampagne_IdCampagne(idCampagne);
    
        if (contributions.isEmpty())
            throw new NoContentException("Aucune contribution trouvée pour cette campagne");
    
        Map<String, ContributionSummaryDto> map = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
        for (Contribution contribution : contributions) {
            Utilisateur user = contribution.getContributeur();
            String userId = user.getIdUtilisateur();
            LocalDateTime dateContribution = LocalDateTime.parse(contribution.getDateContribution(), formatter);
    
            map.compute(userId, (id, summary) -> {
                if (summary == null) {
                    return new ContributionSummaryDto(user, contribution.getMontant(), dateContribution);
                } else {
                    summary.setMontantTotal(summary.getMontantTotal().add(contribution.getMontant()));
                    if (dateContribution.isAfter(summary.getDerniereDate())) {
                        summary.setDerniereDate(dateContribution);
                    }
                    return summary;
                }
            });
        }
    
        return map.values().stream()
                .sorted(Comparator.comparing(ContributionSummaryDto::getDerniereDate).reversed())
                .collect(Collectors.toList());
    }
    
    
    public int getNombreContribution(){
        List<Contribution> contributionListes = contributionRepository.findAll();
        return contributionListes.size();
    }

     public Contribution findContributionByIdContribution(String idContribution){
        Contribution contribution = contributionRepository.findById(idContribution).orElseThrow(() -> new NoContentException("Contribution non trouvée") );

        if(contribution == null)
            throw new NoContentException("Campagne non trouver");
        return contribution;
    }

    public String deleteContibution(String idContribution){
        Contribution contribution = contributionRepository.findById(idContribution).orElseThrow(() -> new NoContentException("Contribution non trouvée") );

        if(contribution == null)
            throw new NoContentException("Contribution non trouver");
            contributionRepository.delete(contribution);
        return "Contribution supprimé avec succèss";
    }
    
}
