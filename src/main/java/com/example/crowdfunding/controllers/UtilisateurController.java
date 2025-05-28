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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.crowdfunding.dto.AuthResponse;
import com.example.crowdfunding.exception.NoContentException;
import com.example.crowdfunding.models.Utilisateur;
import com.example.crowdfunding.repository.UtilisateurRepository;
import com.example.crowdfunding.security.JwtUtil;
import com.example.crowdfunding.services.UtilisateurService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api-crowdfunding/utilisateur")
public class UtilisateurController {

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    JwtUtil  jwtUtil;

     @PostMapping("/create")
    @Operation(summary = "Création d'un utilisateur")
    public ResponseEntity<Utilisateur> createUtilisateur(
            @Valid @RequestParam("utilisateur") String utilisateurString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile)
            throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = new JsonMapper().readValue(utilisateurString, Utilisateur.class);
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }

        Utilisateur savedUtilisateur = utilisateurService.inscriptionUtilisateur(utilisateur, imageFile);

        return new ResponseEntity<>(savedUtilisateur, HttpStatus.OK);
    }

     @PutMapping("/update/{idUtilisateur}")
    @Operation(summary = "Mise à jour d'un utilisateur par son Id ")
    public ResponseEntity<Utilisateur> updateUtilisateur(
            @PathVariable String idUtilisateur,
            @Valid @RequestParam("utilisateur") String utilisateurString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = new JsonMapper().readValue(utilisateurString, Utilisateur.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Utilisateur utilisateurMisAjour = utilisateurService.modifierUtilisateur(utilisateur, idUtilisateur,imageFile);
            return new ResponseEntity<>(utilisateurMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/token/refresh/{refreshToken}")
    public ResponseEntity<AuthResponse> refreshToken(@PathVariable String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(401).build();
        }

        String email = jwtUtil.extractEmail(refreshToken);
        Utilisateur user = utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new NoContentException("Utilisateur non trouvé"));

        // Génère un nouvel access token
        String newAccessToken = jwtUtil.generateAccessToken(user);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
    }

     @GetMapping("/getAllUser")
    @Operation(summary="Liste de tous les utilisateurs")
    public ResponseEntity<List<Utilisateur>> getAll(){
        return new ResponseEntity<>(utilisateurService.getAllUser(), HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        return utilisateurService.login(email,password);
    }

     @GetMapping("/getUtilisateurByIdUtilisateur/{idUtilisateur}")
    @Operation(summary="Recupérer un utilisateur par son id")
    public ResponseEntity<Utilisateur> getUtilisateurByIdUtilisateur(
        @PathVariable String idUtilisateur
    ){
        return new ResponseEntity<>(utilisateurService.findUtilisateurByIdUtilisateur(idUtilisateur), HttpStatus.OK);
    }

    @PutMapping("/activer/{idUtilisateur}")
    @Operation(summary="Activation un user")
    public ResponseEntity<Utilisateur> activeUser(@PathVariable String idUtilisateur) throws Exception {
        return new ResponseEntity<>(utilisateurService.active(idUtilisateur), HttpStatus.OK);
    }


    @PutMapping("/desactiver/{idUtilisateur}")
    @Operation(summary="Desactivation d'un user")
    public ResponseEntity<Utilisateur> desactiveUser(@PathVariable String idUtilisateur) throws Exception {
        return new ResponseEntity<>(utilisateurService.desactive(idUtilisateur), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idUtilisateur}")
    @Operation(summary="Supprimer un utilisateur") 
    public ResponseEntity<Void> deleteUser(@PathVariable("idUtilisateur") String idUtilisateur) {
        utilisateurService.deleteUser(idUtilisateur);
        return  new ResponseEntity<>(HttpStatus.OK); 
    }

    
}
