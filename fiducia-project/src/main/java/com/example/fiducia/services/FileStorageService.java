package com.example.fiducia.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    // Utilisation d'un chemin absolu ou normalisé pour éviter les surprises
    private final Path root = Paths.get("uploads").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            // Utilisation de createDirectories (plus robuste que createDirectory)
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier d'upload : " + e.getMessage());
        }
    }

    public String save(MultipartFile file, String prefix) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Le fichier envoyé est vide.");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new RuntimeException("Nom de fichier invalide.");
            }

            // Extraction et nettoyage du nom/extension
            String cleanFilename = originalFilename.replaceAll("[^a-zA-Z0-String0-9\\.\\-_]", "_");
            String lowerName = cleanFilename.toLowerCase();

            // Validation stricte des extensions
            if (!lowerName.endsWith(".pdf") && !lowerName.endsWith(".jpg") && !lowerName.endsWith(".jpeg") && !lowerName.endsWith(".png")) {
                throw new RuntimeException("Format de fichier non supporté (PDF, JPG, JPEG, PNG uniquement)");
            }

            // Génération d'un nom unique et sécurisé
            String newFileName = prefix + "_" + UUID.randomUUID().toString() + "_" + cleanFilename;

            // Résolution du chemin complet et sécurisation contre les attaques de type Directory Traversal
            Path targetLocation = this.root.resolve(newFileName).normalize();
            if (!targetLocation.getParent().equals(this.root)) {
                throw new RuntimeException("Tentative d'upload en dehors du dossier autorisé.");
            }

            // Sauvegarde physique
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return newFileName;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du stockage du fichier : " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            // Sécurisation du nom de fichier demandé
            Path file = root.resolve(filename).normalize();
            if (!file.getParent().equals(this.root)) {
                throw new RuntimeException("Accès non autorisé à ce chemin.");
            }

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Le fichier n'existe pas ou n'est pas lisible : " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erreur d'accès au fichier : " + e.getMessage());
        }
    }
}