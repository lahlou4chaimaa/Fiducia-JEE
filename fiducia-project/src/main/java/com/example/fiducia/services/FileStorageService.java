package com.example.fiducia.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    public FileStorageService() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier d'upload");
        }
    }

    public String save(MultipartFile file, String prefix) {
        try {
            // Vérification extension
            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".pdf") && !filename.endsWith(".jpg") && !filename.endsWith(".png"))) {
                throw new RuntimeException("Format de fichier non supporté (PDF, JPG, PNG uniquement)");
            }

            // Générer un nom unique : ID_DATE_NOM.pdf
            String newFileName = prefix + "_" + UUID.randomUUID().toString() + "_" + filename;
            Files.copy(file.getInputStream(), this.root.resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);

            return newFileName;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du stockage du fichier : " + e.getMessage());
        }
    }
}