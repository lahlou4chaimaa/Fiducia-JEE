package com.example.fiducia.controllers;

import com.example.fiducia.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class DocumentController {

    private final FileStorageService fileStorageService;

    // Injection de ton service
    public DocumentController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // Cette URL te permettra de visualiser le document
    @GetMapping("/documents/{filename:.+}")
    public ResponseEntity<Resource> visualiserDocument(@PathVariable String filename) {
        // 1. On charge le fichier grâce à ton service
        Resource file = fileStorageService.load(filename);

        // 2. On détermine dynamiquement le type (PDF, PNG, etc.) pour le navigateur
        String contentType = "application/octet-stream";
        if (filename.toLowerCase().endsWith(".pdf")) {
            contentType = "application/pdf";
        } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (filename.toLowerCase().endsWith(".png")) {
            contentType = "image/png";
        }

        // 3. On renvoie le fichier au navigateur
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                // "inline" permet d'afficher le document dans le navigateur au lieu de le télécharger directement
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}