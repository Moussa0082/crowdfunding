package com.example.crowdfunding.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.mojtabaJ.cwebp.WebpConverter;
import jakarta.annotation.PostConstruct;

@Service
public class ImageService {

    public String convertirEtSauvegarderEnWebp(MultipartFile imageFile, String entityId) {
    if (imageFile == null || imageFile.isEmpty()) {
        System.err.println("❌ Fichier image vide ou nul.");
        return null;
    }

    // Définir le répertoire de base pour les uploads
    String baseUploadDir = "uploads";
    // Créer le chemin spécifique à l'entité (par exemple, "uploads/utilisateurs/123/")
    String entityUploadDir = baseUploadDir + "/utilisateurs/" + entityId;

    // Créer le dossier s'il n'existe pas
    File uploadDir = new File(entityUploadDir);
    if (!uploadDir.exists()) {
        if (!uploadDir.mkdirs()) {
            System.err.println("❌ Impossible de créer le dossier de destination : " + uploadDir.getAbsolutePath());
            return null; // Arrêt si la création du dossier échoue
        }
        System.out.println("📁 Dossier créé : " + uploadDir.getAbsolutePath());
    }

    // Générer un nom de fichier unique pour éviter les collisions
    String originalFileName = imageFile.getOriginalFilename();
    String fileExtension = "";
    String baseName = originalFileName;

    if (originalFileName != null && originalFileName.contains(".")) {
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
    }
    String uniqueFileName = baseName + "-" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;
    String inputImagePath = entityUploadDir + "/" + uniqueFileName; // Chemin de sauvegarde de l'original
    String outputImagePath = entityUploadDir + "/" + baseName + "-" + UUID.randomUUID().toString().substring(0, 8) + ".webp"; // Chemin du WebP

    int quality = 80; // Qualité de la conversion WebP

    try {
        // 1. Sauvegarder l'image originale
        Path inputPath = Paths.get(inputImagePath);
        Files.write(inputPath, imageFile.getBytes());
        System.out.println("✅ Image originale sauvegardée : " + inputPath.toAbsolutePath());

        // 2. Convertir en WebP
        File webpFile = WebpConverter.imageFileToWebpFile(inputImagePath, outputImagePath, quality);
        if (webpFile == null) {
            System.err.println("❌ La conversion WebP a échoué.");
            return null;
        }
        System.out.println("✅ Image convertie en WebP : " + webpFile.getAbsolutePath());
        // 3. Supprimer l'image originale
        Files.delete(inputPath);
        System.out.println("🗑️  Image originale supprimée : " + inputPath.toAbsolutePath());

        return webpFile.getAbsolutePath(); // Retourner le chemin du fichier WebP

    } catch (IOException e) {
        System.err.println("❌ Erreur lors du traitement de l'image : " + e.getMessage());
        return null; // Gestion des erreurs
    } catch (Exception e) {
        System.err.println("❌ Erreur inattendue : " + e.getMessage());
        return null;
    }
    }
    //Méthode pour récupérer l'image webp par Id
    public byte[] getImageWebp(String entityId, String imageName) throws IOException{

    String baseUploadDir = "uploads";
    String entityUploadDir = baseUploadDir + "/utilisateurs/" + entityId;
    String imagePath = entityUploadDir + "/" + imageName;
    File imageFile = new File(imagePath);
     if (!imageFile.exists()) {
        System.err.println("❌ Fichier non trouvé : " + imagePath);
        return null;
    }
    Path path = Paths.get(imagePath);
    return Files.readAllBytes(path);

    }


    // Méthode utilitaire pour générer un ID unique si nécessaire
    public String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    
    public byte[] getImageById(String objectId) throws IOException {
        File imageFile = new File("uploads/utilisateurs/" + objectId + ".webp");
        if (!imageFile.exists()) {
            throw new FileNotFoundException("Image non trouvée : " + imageFile.getAbsolutePath());
        }
        return Files.readAllBytes(imageFile.toPath());
    }   

    
}
