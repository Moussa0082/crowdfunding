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
import com.example.crowdfunding.models.Categorie;
import com.example.crowdfunding.repository.CategorieRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategorieService {


     @Autowired
    CategorieRepository categorieRepository;

    @Autowired
    ImageService imageService;


    String pattern = "yyyy-MM-dd HH:mm";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(formatter);

     public Categorie ajouterCategorie(Categorie categorie, MultipartFile photo) throws IOException{
 
        Categorie c = categorieRepository.findByNomCategorie(categorie.getNomCategorie());

        if(c != null)
        throw new AlreadyExistsException("Cette categorie existe déjà");

        categorie.setIdCategorie(UUID.randomUUID().toString());

        if (photo != null && !photo.isEmpty()) {
            String imageBase64 = imageService.convertirEtSauvegarderEnWebp(photo, categorie.getIdCategorie());
            categorie.setImage(imageBase64); 
        }

        categorie.setDateCreation(formattedDateTime);        
        Categorie savedCategorie = categorieRepository.save(categorie);
        return savedCategorie;
    }


     public Categorie modifierCategorie(Categorie categorie, String idCategorie,  MultipartFile photo) throws IOException{
 
        Categorie c = categorieRepository.findByNomCategorie(categorie.getNomCategorie());

        if(c == null)
        throw new NoContentException("Cette categorie n'existe pas");

        if (photo != null && !photo.isEmpty()) {
            String imageBase64 = imageService.convertirEtSauvegarderEnWebp(photo, categorie.getIdCategorie());
            c.setImage(imageBase64); 
        }

        c.setNomCategorie(categorie.getNomCategorie());           
        c.setDescription(categorie.getDescription());           
        c.setDateModif(formattedDateTime);        
        Categorie savedCategorie = categorieRepository.save(categorie);
        return savedCategorie;
    }


    public List<Categorie> getAllCategorie() {
        List<Categorie> categories = categorieRepository.findAll();
    
        if (categories.isEmpty())
            throw new EntityNotFoundException("Aucune categorie trouvée");

        categories.sort(Comparator.comparing(Categorie::getDateCreation).reversed());
        
        return categories;
    }



    public String deleteCategorie(String idCategorie){
        Categorie categorie = categorieRepository.findById(idCategorie).orElseThrow(() -> new IllegalStateException("Categorie non trouvé") );

        if(categorie == null)
            throw new NoContentException("Campagne non trouver");
            categorieRepository.delete(categorie);
        return "Categorie supprimée avec succèss";
    }
    
}
