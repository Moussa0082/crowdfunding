package com.example.crowdfunding.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.crowdfunding.dto.AuthResponse;
import com.example.crowdfunding.exception.AlreadyExistsException;
import com.example.crowdfunding.exception.NoContentException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.crowdfunding.models.RoleUtilisateur;
import com.example.crowdfunding.models.Utilisateur;
import com.example.crowdfunding.repository.UtilisateurRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UtilisateurService {

    @Autowired
    UtilisateurRepository utilisateurRepository;


    @Autowired
    ImageService imageService;


    @Autowired
    TokenService tokenService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    String pattern = "yyyy-MM-dd HH:mm";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(formatter);

     public Utilisateur inscriptionUtilisateur(Utilisateur utilisateur , MultipartFile photo) throws IOException{
 
        Utilisateur u = utilisateurRepository.findByEmailOrNumero(utilisateur.getEmail(), utilisateur.getNumero());

        String passWordHasher = passwordEncoder.encode(utilisateur.getPassword());
        utilisateur.setPassword(passWordHasher);
        utilisateur.setRoleUtilisateur(RoleUtilisateur.USER);

        if(u != null)
        throw new AlreadyExistsException("Cet utilisateur existe déjà");

        utilisateur.setIdUtilisateur(UUID.randomUUID().toString());

        if (photo != null && !photo.isEmpty()) {
            String imageBase64 = imageService.convertirEtSauvegarderEnWebp(photo, utilisateur.getIdUtilisateur());
            utilisateur.setPhoto(imageBase64); 
        }

        utilisateur.setDateInscription(formattedDateTime);        
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        return savedUtilisateur;
    }


     public ResponseEntity<?> login(Utilisateur u) {
        Optional<Utilisateur> userOptional = utilisateurRepository.findByEmail(u.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Utilisateur introuvable");
        }

        Utilisateur user = userOptional.get();

        if (!passwordEncoder.matches(u.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Mot de passe incorrect");
        }

        AuthResponse tokens = tokenService.generateTokens(user.getEmail());
        return ResponseEntity.ok(tokens);
    }


     public Utilisateur modifierUtilisateur(Utilisateur utilisateur ,String idUtilisateur, MultipartFile photo) throws IOException{
 
        Utilisateur u = utilisateurRepository.findById(idUtilisateur).orElseThrow(() -> new NoContentException("Utilisateur non trouvé") );

        String passWordHasher = passwordEncoder.encode(utilisateur.getPassword());
        if(utilisateur.getPassword() != null){
            u.setPassword(passWordHasher);
        }

        if(u == null)
        throw new NoContentException("Cet utilisateur n'existe pas");

        u.setPrenom(utilisateur.getPrenom());
        u.setNom(utilisateur.getNom());
        u.setNumero(utilisateur.getNumero());
        u.setAddresse(utilisateur.getAddresse());
        u.setEmail(utilisateur.getEmail());
  
        if (photo != null && !photo.isEmpty()) {
            String imageBase64 = imageService.convertirEtSauvegarderEnWebp(photo, utilisateur.getIdUtilisateur());
            u.setPhoto(imageBase64); 
        }
        if (utilisateur.getRoleUtilisateur() != null) {
            u.setRoleUtilisateur(utilisateur.getRoleUtilisateur()); 
        }

        u.setDateModif(formattedDateTime);        
        Utilisateur savedUtilisateur = utilisateurRepository.save(u);
        return savedUtilisateur;
    }

       
    public Utilisateur findUtilisateurByIdUtilisateur(String idUtilisateur){
        Utilisateur user = utilisateurRepository.findById(idUtilisateur).orElseThrow(() -> new NoContentException("Utilisateur non trouvé") );

        if(user == null)
            throw new NoContentException("Utilisateur non trouver");
        return user;
    }

    public String deleteUser(String idUtilisateur){
        Utilisateur user = utilisateurRepository.findById(idUtilisateur).orElseThrow(() -> new NoContentException("Utilisateur non trouvé") );

        if(user == null)
            throw new NoContentException("Utilisateur non trouver");
            utilisateurRepository.delete(user);
        return "Utilisateur supprimé avec succèss";
    }

    public Utilisateur active(String idUtilisateur) throws Exception{
        Utilisateur user = utilisateurRepository.findById(idUtilisateur).orElseThrow(null);

        try {
            user.setActif(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation du User: " + e.getMessage());
        }
        return utilisateurRepository.save(user);
    }

    public Utilisateur desactive(String idUtilisateur) throws Exception{
        Utilisateur user = utilisateurRepository.findById(idUtilisateur).orElseThrow(null);

        try {
            user.setActif(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation du User : " + e.getMessage());
        }
        return utilisateurRepository.save(user);
    }


    public String changeRoleUtilisateur(String idUtilisateur, RoleUtilisateur role) throws Exception{
        Utilisateur user = utilisateurRepository.findById(idUtilisateur).orElseThrow(null);

        try {
            user.setRoleUtilisateur(role);
        } catch (Exception e) {
            throw new Exception("Erreur lors du changement du role deutilisateur : " + e.getMessage());
        }
        return "Role de l'utilisateur " + user.getPrenom() + user.getNom() + " changé avec succès";
    }


    public List<Utilisateur> getAllUser() {
        List<Utilisateur> users = utilisateurRepository.findAll();
    
        if (users.isEmpty())
            throw new EntityNotFoundException("Aucun utilisateur trouvée");

        users.sort(Comparator.comparing(Utilisateur::getDateInscription).reversed());
        
        return users;
    }

    
}
