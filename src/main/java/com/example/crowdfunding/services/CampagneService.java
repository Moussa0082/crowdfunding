package com.example.crowdfunding.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.crowdfunding.exception.AlreadyExistsException;
import com.example.crowdfunding.exception.NoContentException;
import com.example.crowdfunding.models.Campagne;
import com.example.crowdfunding.models.RoleUtilisateur;
import com.example.crowdfunding.models.Utilisateur;
import com.example.crowdfunding.repository.CampagneRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CampagneService {

    @Autowired
    CampagneRepository campagneRepository;

    @Autowired
    ImageService imageService;


    String pattern = "yyyy-MM-dd HH:mm";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(formatter);

     public Campagne ajouterCampagne(Campagne campagne, MultipartFile photo) throws IOException{
 
        Campagne c = campagneRepository.findByTitreAndCreateur(campagne.getTitre(), campagne.getCreateur());



        if(c != null)
        throw new AlreadyExistsException("Cette campagne existe déjà");

        campagne.setIdCampagne(UUID.randomUUID().toString());

        if (photo != null && !photo.isEmpty()) {
            String imageBase64 = imageService.convertirEtSauvegarderEnWebp(photo, campagne.getIdCampagne());
            campagne.setImageUrl(imageBase64); 
        }

        campagne.setActive(true);        
        campagne.setDateCreation(formattedDateTime);        
        Campagne savedCampagne = campagneRepository.save(campagne);
        return savedCampagne;
    }


     public Campagne modifierCampagne(Campagne campagne, String idCampagne,  MultipartFile photo) throws IOException{
 
        Campagne c = campagneRepository.findById(idCampagne).orElseThrow(() -> new NoContentException("Campagne non trouvé") );

        if(c == null)
        throw new NoContentException("Cette campagne n'existe pas");

        if (photo != null && !photo.isEmpty()) {
            String imageBase64 = imageService.convertirEtSauvegarderEnWebp(photo, campagne.getIdCampagne());
            c.setImageUrl(imageBase64); 
        }

        c.setTitre(campagne.getTitre());        
        c.setDescription(campagne.getDescription());        
        c.setMontantActuel(campagne.getMontantActuel());        
        c.setMontantCible(campagne.getMontantCible());        
        c.setDateModif(formattedDateTime);        
        Campagne savedCampagne = campagneRepository.save(c);
        return savedCampagne;
    }


    public List<Campagne> getAllCampagne() {
        List<Campagne> campagnes = campagneRepository.findAll();
    
        if (campagnes.isEmpty())
            throw new EntityNotFoundException("Aucune campagne trouvée");

        campagnes.sort(Comparator.comparing(Campagne::getDateCreation).reversed());
        
        return campagnes;
    }

    public List<Campagne> getAllCampagneByUtilisateur(String idUtilisateur) {
        List<Campagne> campagnes = campagneRepository.findAllByCreateurIdUtilisateur(idUtilisateur);
    
        if (campagnes.isEmpty())
            throw new NoContentException("Aucune campagne trouvée");

        campagnes.sort(Comparator.comparing(Campagne::getDateCreation).reversed());
        
        return campagnes;
    }

    public List<Campagne> getAllCampagneByCategorie(String idCategorie) {
        List<Campagne> campagnes = campagneRepository.findAllByCategorieIdCategorie(idCategorie);
    
        if (campagnes.isEmpty())
            throw new NoContentException("Aucune campagne trouvée");

        campagnes.sort(Comparator.comparing(Campagne::getDateCreation).reversed());
        
        return campagnes;
    }


     public Campagne findCampagneByIdCampagne(String idCampagne){
        Campagne campagne = campagneRepository.findById(idCampagne).orElseThrow(() -> new NoContentException("Campagne non trouvé"));

        if(campagne == null)
            throw new NoContentException("Campagne non trouver");
        return campagne;
    }

    public String deleteCampagne(String idCampagne){
        Campagne campagne = campagneRepository.findById(idCampagne).orElseThrow(() -> new NoContentException("Campagne non trouvé") );

        if(campagne == null)
            throw new NoContentException("Campagne non trouver");
            campagneRepository.delete(campagne);
        return "Campagne supprimé avec succèss";
    }

    public Campagne active(String idCampagne) throws Exception{
        Campagne campagne = campagneRepository.findById(idCampagne).orElseThrow(() -> new NoContentException("Campagne non trouvé"));

        try {
            campagne.setActive(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation de la campagne: " + e.getMessage());
        }
        return campagneRepository.save(campagne);
    }

    public Campagne desactive(String idCampagne) throws Exception{
        Campagne campagne = campagneRepository.findById(idCampagne).orElseThrow(() -> new NoContentException("Campagne non trouvé"));

        try {
            campagne.setActive(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation de la campagne : " + e.getMessage());
        }
        return campagneRepository.save(campagne);
    }

    
}
