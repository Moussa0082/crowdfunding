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

import com.example.crowdfunding.models.Categorie;
import com.example.crowdfunding.services.CategorieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api-crowdfunding/categorie")
public class CategorieController {

  @Autowired
  CategorieService categorieService;


    @PostMapping("/create")
    @Operation(summary = "Création d'une categorie")
    public ResponseEntity<Categorie> createCategorie(
            @Valid @RequestParam("categorie") String categorieString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile)
            throws Exception {
        Categorie categorie = new Categorie();
        try {
            categorie = new JsonMapper().readValue(categorieString, Categorie.class);
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }

        Categorie savedCategorie = categorieService.ajouterCategorie(categorie, imageFile);

        return new ResponseEntity<>(savedCategorie, HttpStatus.OK);
    }


    @PutMapping("/update/{idCategorie}")
    @Operation(summary = "Mise à jour d'une categorie par son Id ")
//     public ResponseEntity<?> updateCategorie(
//         @PathVariable String idCategorie,
//         @Valid @RequestParam("categorie") String categorieString,
//         @RequestParam(value = "image", required = false) MultipartFile imageFile) {

//     try {
//         // Convertit la chaîne JSON en objet Java
//         Categorie categorie = new ObjectMapper().readValue(categorieString, Categorie.class);

//         // Appelle le service pour la mise à jour
//         Categorie categorieMisAjour = categorieService.modifierCategorie(categorie, idCategorie, imageFile);

//         return ResponseEntity.ok(categorieMisAjour);

//     } catch (JsonProcessingException e) {
//         return ResponseEntity
//                 .badRequest()
//                 .body("Erreur de format JSON pour 'categorie' : " + e.getOriginalMessage());

//     } catch (Exception e) {
//         return ResponseEntity
//                 .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                 .body("Erreur serveur lors de la mise à jour de la catégorie : " + e.getMessage());
//     }
// }
public ResponseEntity<?> updateCategorie(
    @PathVariable String idCategorie,
    @Valid @RequestParam("categorie") String categorieString, // Expecting a JSON string here
    @RequestParam(value = "image", required = false) MultipartFile imageFile) {

Categorie categorie = new Categorie();
try {
    // Attempt to deserialize the JSON string into a Categorie object
    categorie = new JsonMapper().readValue(categorieString, Categorie.class);
} catch (JsonProcessingException e) {
    // If the string is not valid JSON, return a Bad Request error
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("Le format des données de la catégorie est invalide. Veuillez fournir un JSON valide.");
}

try {
    // Call your service to modify the category
    Categorie categorieMisAjour = categorieService.modifierCategorie(categorie, idCategorie, imageFile);
    return new ResponseEntity<>(categorieMisAjour, HttpStatus.OK);
} catch (Exception e) {
    // Catch broader exceptions from the service layer for server errors
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Erreur serveur lors de la mise à jour de la catégorie : " + e.getMessage());
}
}

    // public ResponseEntity<?> updateCategorie(
    //         @PathVariable String idCategorie,
    //         @Valid @RequestParam("categorie") String categorieString,
    //         @RequestParam(value = "image", required = false) MultipartFile imageFile) {
    //     Categorie categorie = new Categorie();
    //     try {
    //         categorie = new JsonMapper().readValue(categorieString, Categorie.class);
    //     } catch (JsonProcessingException e) {
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }

    //     try {
    //         Categorie categorieMisAjour = categorieService.modifierCategorie(categorie, idCategorie,imageFile);
    //         return new ResponseEntity<>(categorieMisAjour, HttpStatus.OK);
    //     } catch (Exception e) {
    //         return  ResponseEntity
    //             .status(HttpStatus.INTERNAL_SERVER_ERROR)
    //             .body("Erreur serveur lors de la mise à jour de la catégorie : " + e.getMessage());
    //     }
    // }

    @GetMapping("/getAllCategorie")
    @Operation(summary="Liste de tout les categories")
    public ResponseEntity<List<Categorie>> getAllCategorie(){
        return new ResponseEntity<>(categorieService.getAllCategorie(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary="Supprimé une categorie")
    public ResponseEntity<Void> deleteCategorie(@PathVariable("id") String id) {
        categorieService.deleteCategorie(id);
        return  new ResponseEntity<>(HttpStatus.OK); 
    }
    
}
