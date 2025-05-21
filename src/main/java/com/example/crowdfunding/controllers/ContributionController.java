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

import com.example.crowdfunding.dto.ContributionSummaryDto;
import com.example.crowdfunding.models.Contribution;
import com.example.crowdfunding.services.ContributionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api-crowdfunding/contribution")
public class ContributionController {

  @Autowired
  ContributionService contributionService;

    
    @PostMapping("/create/{idCampagne}")
    @Operation(summary = "Création d'une contribution")
    public ResponseEntity<Contribution> createContribution(
            @Valid @RequestParam("contribution") String contributionString,
            @PathVariable String idCampagne,
            @RequestParam(value = "image", required = false) MultipartFile imageFile)
            throws Exception {
        Contribution contribution = new Contribution();
        try {
            contribution = new JsonMapper().readValue(contributionString, Contribution.class);
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }

        Contribution savedContribution = contributionService.ajouterContribution(contribution, idCampagne, imageFile);

        return new ResponseEntity<>(savedContribution, HttpStatus.OK);
    }


    @PutMapping("/update/{idContribution}")
    @Operation(summary = "Mise à jour d'une contribution  par son Id ")
    public ResponseEntity<Contribution> updateContribution(
            @PathVariable String idContribution,
            @Valid @RequestParam("contribution") String contributionString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
                Contribution contribution = new Contribution();
        try {
            contribution = new JsonMapper().readValue(contributionString, Contribution.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Contribution contributionMisAjour = contributionService.modifierContribution(contribution, idContribution,imageFile);
            return new ResponseEntity<>(contributionMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllContribution")
    @Operation(summary="Liste de tout les contributions")
    public ResponseEntity<List<Contribution>> getAllContribution(){
        return new ResponseEntity<>(contributionService.getAllContribution(), HttpStatus.OK);
    }

    @GetMapping("/getAllUtilisateurByCampagne/{idCampagne}")
    @Operation(summary="Liste de tout les contributeurs par campagne")
    public ResponseEntity<List<ContributionSummaryDto>> getAllUtilisateurByContribution(
        @PathVariable String idCampagne
    ){
        return new ResponseEntity<>(contributionService.getContributionSummaryByCampagne(idCampagne), HttpStatus.OK);
    }


    @GetMapping("/getContributionByIdContribution/{idContribution}")
    @Operation(summary="Liste de tout les contributeurs par campagne")
    public ResponseEntity<Contribution> getContributionByIdContribution(
        @PathVariable String idContribution
    ){
        return new ResponseEntity<>(contributionService.findContributionByIdContribution(idContribution), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary="Supprimé une contribution")
    public ResponseEntity<Void> deleteContribution(@PathVariable("id") String id) {
        contributionService.deleteContibution(id);
        return  new ResponseEntity<>(HttpStatus.OK); 
    }
    
    
}
