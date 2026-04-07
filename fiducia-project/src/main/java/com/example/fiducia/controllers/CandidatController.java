package com.example.fiducia.controllers;

import com.example.fiducia.dto.DossierStatusDTO;
import com.example.fiducia.entities.CandidatDoctorant;
import com.example.fiducia.entities.DirecteurThese;
import com.example.fiducia.entities.DossierInscription;
import com.example.fiducia.entities.StatutDossier; // Important
import com.example.fiducia.repositories.DirecteurTheseRepository;
import com.example.fiducia.repositories.DossierInscriptionRepository;
import com.example.fiducia.repositories.UserRepository;
import com.example.fiducia.services.DossierInscriptionService;
import com.example.fiducia.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/candidat")
public class CandidatController {

    @Autowired private FileStorageService fileService;
    @Autowired private DossierInscriptionRepository dossierRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private DossierInscriptionService dossierService;
    @Autowired
    private DirecteurTheseRepository directeurRepository;

    @PostMapping("/soumettre-dossier")
    public ResponseEntity<?> soumettreDossier(
            @RequestParam("sujet") String sujet,
            @RequestParam("laboratoire") String laboratoire, // Ajouté
            @RequestParam("directeurId") Long directeurId,
            @RequestParam("cv") MultipartFile cv,
            @RequestParam("collaboration") String collaboration,
            @RequestParam("diplome") MultipartFile diplome,
            @RequestParam("lettre") MultipartFile lettre, // Ajouté
            Principal principal) {

        // 1. Trouver le candidat
        CandidatDoctorant candidat = (CandidatDoctorant) userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));

        // 2. Sauvegarder les fichiers
        String cvName = fileService.save(cv, "CV_" + candidat.getNom());
        String diplomeName = fileService.save(diplome, "DIPLOME_" + candidat.getNom());
        String lettreName = fileService.save(lettre, "LETTRE_" + candidat.getNom());

        // 3. Créer le dossier
        DossierInscription dossier = new DossierInscription();
        dossier.setCandidat(candidat);
        dossier.setSujetThèse(sujet);
        dossier.setLaboratoire(laboratoire);
        dossier.setCollaboration(collaboration);

        // Trouver le directeur
        DirecteurThese dt = (DirecteurThese) directeurRepository.findById(directeurId)
                .orElseThrow(() -> new RuntimeException("L'utilisateur avec l'ID " + directeurId + " n'est pas enregistré comme Directeur de thèse."));
        dossier.setDirecteurThese(dt);

        dossier.setCvPath(cvName);
        dossier.setDiplomePath(diplomeName);
        dossier.setLettreMotivationPath(lettreName);

        // CORRECTION : Utiliser l'Enum au lieu du String "SOUMIS"
        dossier.setStatut(StatutDossier.SOUMIS);
        dossier.setDateDerniereModification(LocalDateTime.now());

        dossierRepository.save(dossier);

        return ResponseEntity.ok("Dossier complet soumis avec succès !");
    }

    @GetMapping("/mon-statut")
    public ResponseEntity<?> getMonDossier(Principal principal) {
        CandidatDoctorant c = (CandidatDoctorant) userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));

        Optional<DossierInscription> dossierOpt = dossierRepository.findByCandidat(c);

        if (dossierOpt.isPresent()) {
            return ResponseEntity.ok(dossierOpt.get());
        } else {
            // Il est préférable de renvoyer un objet cohérent ou une chaîne simple
            return ResponseEntity.ok("Aucun dossier en cours.");
        }
    }

    @GetMapping("/mon-suivi")
    public ResponseEntity<DossierStatusDTO> suivreMonDossier(Principal principal) {
        CandidatDoctorant candidat = (CandidatDoctorant) userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        DossierStatusDTO suivi = dossierService.obtenirSuiviCandidat(candidat);
        return ResponseEntity.ok(suivi);
    }
}