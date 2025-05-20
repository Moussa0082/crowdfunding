package com.example.crowdfunding.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.crowdfunding.models.Campagne;
import com.example.crowdfunding.services.CampagneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api-crowdfunding/campagne")
public class CampagneController {

    @Autowired
    CampagneService campagneService;


      @PostMapping("/create")
    @Operation(summary = "Création d'une campagne")
    public ResponseEntity<Campagne> createCampagne(
            @Valid @RequestParam("campagne") String campagneString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile)
            throws Exception {
        Campagne campagne = new Campagne();
        try {
            campagne = new JsonMapper().readValue(campagneString, Campagne.class);
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }

        Campagne savedCampagne = campagneService.ajouterCampagne(campagne, imageFile);

        return new ResponseEntity<>(savedCampagne, HttpStatus.OK);
    }


    @PutMapping("/update/{idCampagne}")
    @Operation(summary = "Mise à jour d'une campagne par son Id ")
    public ResponseEntity<Campagne> updateCampagne(
            @PathVariable String idCampagne,
            @Valid @RequestParam("campagne") String campagneString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        Campagne campagne = new Campagne();
        try {
            campagne = new JsonMapper().readValue(campagneString, Campagne.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Campagne campagneMisAjour = campagneService.modifierCampagne(campagne, idCampagne,imageFile);
            return new ResponseEntity<>(campagneMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


     @GetMapping("/getAllCampagne")
    @Operation(summary="Liste de tous les campagnes")
    public ResponseEntity<List<Campagne>> getAllCampagnes(){
        return new ResponseEntity<>(campagneService.getAllCampagne(), HttpStatus.OK);
    }

     @GetMapping("/getAllCampagnesByUtilisateur/{idUtilisateur}")
    @Operation(summary="Liste de tous les campagnes par utilisateur")
    public ResponseEntity<List<Campagne>> getAllCampagnesByUser(
        @PathVariable String idUtilisateur
    ){
        return new ResponseEntity<>(campagneService.getAllCampagneByUtilisateur(idUtilisateur), HttpStatus.OK);
    }

     @GetMapping("/getAllCampagnesByCategorie/{idCategorie}")
    @Operation(summary="Liste de tous les campagnes par categorie")
    public ResponseEntity<List<Campagne>> getAllCampagnesByCategorie(
        @PathVariable String idCategorie
    ){
        return new ResponseEntity<>(campagneService.getAllCampagneByCategorie(idCategorie), HttpStatus.OK);
    }

     @GetMapping("/getCampagneByIdCampagne/{idCampagne}")
    @Operation(summary="Recupérer un campagne par son id")
    public ResponseEntity<Campagne> getCampagneByIdCampagne(
        @PathVariable String idCampagne
    ){
        return new ResponseEntity<>(campagneService.findCampagneByIdCampagne(idCampagne), HttpStatus.OK);
    }

    @PutMapping("/activer/{idCampagne}")
    @Operation(summary="Activation d'une campagne")
    public ResponseEntity<Campagne> activeCampagne(@PathVariable String idCampagne) throws Exception {
        return new ResponseEntity<>(campagneService.active(idCampagne), HttpStatus.OK);
    }

    @PutMapping("/desactiver/{idCampagne}")
    @Operation(summary="Desactivation d'une campagne")
    public ResponseEntity<Campagne> desactiveCampagne(@PathVariable String idCampagne) throws Exception {
        return new ResponseEntity<>(campagneService.desactive(idCampagne), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idCampagne}")
    @Operation(summary="Supprimer une campagne") 
    public ResponseEntity<Void> deleteCampagne(@PathVariable("idCampagne") String idCampagne) {
        campagneService.deleteCampagne(idCampagne);
        return  new ResponseEntity<>(HttpStatus.OK); 
    }
    
}
