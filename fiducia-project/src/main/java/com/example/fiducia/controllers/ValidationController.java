package com.example.fiducia.controllers;

import com.example.fiducia.dto.AvisRequest;
import com.example.fiducia.dto.ValidationFinaleRequest;
import com.example.fiducia.entities.DossierInscription;
import com.example.fiducia.entities.StatutDossier; // Import de l'Enum
import com.example.fiducia.repositories.DossierInscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/validation")
public class ValidationController {

    @Autowired
    private DossierInscriptionRepository dossierRepo;

    // Étape 2 : Avis du Directeur
    @PreAuthorize("hasRole('DIRECTEUR')")
    @PostMapping("/directeur/{dossierId}/avis")
    public ResponseEntity<?> avisDirecteur(
            @PathVariable Long dossierId,
            @RequestBody Map<String, Object> body) { // On prend tout du Body

        DossierInscription d = dossierRepo.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        // On récupère "valide" depuis le JSON (par défaut false si absent)
        boolean estValide = body.get("valide") != null && (boolean) body.get("valide");

        // 1. Gestion du statut
        if (estValide) {
            d.setStatut(StatutDossier.EN_ATTENTE_ADMIN);
            d.setAvisDirecteur("FAVORABLE");
        } else {
            d.setStatut(StatutDossier.REJETE);
            d.setAvisDirecteur("DEFAVORABLE");
        }

        // 2. Gestion du commentaire
        if (body.containsKey("commentaires")) {
            d.setCommentaireDirecteur((String) body.get("commentaires"));
        }

        d.setDateDerniereModification(LocalDateTime.now());
        dossierRepo.save(d);

        return ResponseEntity.ok("Avis enregistré via Body JSON.");
    }

    // Étape 3 : Validation Administrative (Dernière étape)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/{dossierId}/finaliser")
    public ResponseEntity<?> validationFinale(
            @PathVariable Long dossierId,
            @RequestBody ValidationFinaleRequest request) {

        DossierInscription d = dossierRepo.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        // 1. Mise à jour du statut final
        if (request.isValide()) {
            d.setStatut(StatutDossier.VALIDE);
        } else {
            d.setStatut(StatutDossier.REJETE);
        }

        // 2. Enregistrement de la remarque de l'admin
        d.setRemarquesAdmin(request.getRemarques());

        // 3. Mise à jour de la date et sauvegarde
        d.setDateDerniereModification(LocalDateTime.now());
        dossierRepo.save(d);

        String message = request.isValide() ? "Inscription validée avec succès." : "Dossier refusé par l'administration.";
        return ResponseEntity.ok(message);
    }
}