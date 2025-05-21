package com.example.crowdfunding.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.crowdfunding.services.ImageService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api-crowdfunding/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    public static final MediaType IMAGE_WEBP = MediaType.parseMediaType("image/webp");


    private final String baseUploadDir = "uploads";
    private final String imageSubDir = "utilisateurs"; // Sous-dossier pour les images des utilisateurs

  @GetMapping("/utilisateurs/{entityId}/image/{imageName}")
  @Operation(summary="Récuperer une image par l'id de l'objet à la quelle elle est lié et par son nom")
    public ResponseEntity<byte[]> getImage(@PathVariable String entityId, @PathVariable String imageName) throws IOException {
        String imagePath = baseUploadDir + "/" + imageSubDir + "/" + entityId + "/" + imageName;
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            System.err.println("❌ Fichier non trouvé : " + imagePath);
            return ResponseEntity.notFound().build();
        }
        Path path = Paths.get(imagePath);
        byte[] imageBytes = Files.readAllBytes(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(IMAGE_WEBP);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    
}
