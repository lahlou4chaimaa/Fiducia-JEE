package com.example.fiducia.controllers;

import com.example.fiducia.dto.PlanificationDTO;
import com.example.fiducia.dto.SoutenanceStatusDTO;
import com.example.fiducia.entities.*;
import com.example.fiducia.repositories.DemandeSoutenanceRepository;
import com.example.fiducia.repositories.DossierInscriptionRepository;
import com.example.fiducia.repositories.UserRepository;
import com.example.fiducia.services.FileStorageService;
import com.example.fiducia.services.SoutenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/soutenance")
public class SoutenanceController {

    @Autowired private FileStorageService fileService;
    @Autowired private SoutenanceService soutenanceService;
    @Autowired private DemandeSoutenanceRepository soutenanceRepo;
    @Autowired private UserRepository userRepository;
    @Autowired private DossierInscriptionRepository dossierRepo; // Ajouté pour récupérer le Directeur

    @PostMapping("/soumettre")
    @PreAuthorize("hasAuthority('ROLE_CANDIDAT')")
    public ResponseEntity<?> soumettreDemande(
            @RequestParam("nbArticlesQ1Q2") int nbArticles,
            @RequestParam("nbConferences") int nbConfs,
            @RequestParam("heuresFormation") int heures,
            @RequestParam("demandeManuscrite") MultipartFile demandeManuscrite,
            @RequestParam("rapportThese") MultipartFile rapportThese,
            @RequestParam("antiPlagiat") MultipartFile antiPlagiat,
            @RequestParam("rapportPublisCom") MultipartFile rapportPublisCom,
            @RequestParam("attestationsFormations") MultipartFile attestationsFormations,
            Principal principal) {

        Utilisateur user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!(user instanceof CandidatDoctorant)) {
            return ResponseEntity.badRequest().body("Seul un candidat doctorant peut effectuer cette action.");
        }
        CandidatDoctorant candidat = (CandidatDoctorant) user;

        try {
            // Déclenche les vérifications d'inscription validée + critères (2 articles, 2 confs, 200h)
            soutenanceService.verifierEligibiliteSoutenance(candidat, nbArticles, nbConfs, heures);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // Récupération du dossier d'inscription pour obtenir le directeur rattaché
        DossierInscription dossier = dossierRepo.findByCandidat(candidat)
                .orElseThrow(() -> new RuntimeException("Dossier d'inscription introuvable pour ce candidat."));

        // Correction de la méthode de stockage des fichiers : utilisation de 'save' avec préfixe
        String pathManuscrit = fileService.save(demandeManuscrite, "MANUSCRIT");
        String pathRapport = fileService.save(rapportThese, "RAPPORT");
        String pathPlagiat = fileService.save(antiPlagiat, "PLAGIAT");
        String pathPublis = fileService.save(rapportPublisCom, "PUBLIS");
        String pathAttest = fileService.save(attestationsFormations, "ATTESTATIONS");

        // Instanciation de la demande
        DemandeSoutenance demande = new DemandeSoutenance();
        demande.setDoctorant(candidat);
        demande.setDirecteur(dossier.getDirecteurThese()); // Récupéré depuis le dossier d'inscription valide !

        demande.setNbArticlesQ1Q2(nbArticles);
        demande.setNbConferences(nbConfs);
        demande.setHeuresFormationCompletees(heures);

        demande.setDemandeManuscritePath(pathManuscrit);
        demande.setRapportThesePath(pathRapport);
        demande.setRapportAntiPlagiatPath(pathPlagiat);
        demande.setRapportPublicationsCommunicationsPath(pathPublis);
        demande.setCopiesAttestationsFormationsPath(pathAttest);

        soutenanceService.creerDemande(demande);

        return ResponseEntity.ok("Votre demande de soutenance a été soumise avec succès au Directeur de thèse.");
    }

    @PostMapping("/{id}/proposer-jury")
    @PreAuthorize("hasAuthority('ROLE_DIRECTEUR')")
    public ResponseEntity<?> proposerJury(
            @PathVariable Long id,
            @RequestBody List<String> membres,
            Principal principal) {

        DemandeSoutenance demande = soutenanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (!demande.getDirecteur().getEmail().equals(principal.getName())) {
            return ResponseEntity.status(403).body("Vous n'êtes pas autorisé à gérer cette demande.");
        }

        demande.setMembresJuryProposes(membres);
        demande.setStatut(DemandeSoutenance.StatutSoutenance.JURY_PROPOSE);
        soutenanceRepo.save(demande);

        return ResponseEntity.ok("Proposition du jury transmise avec succès à l'administration.");
    }

    @PostMapping("/{id}/autoriser-et-planifier")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> autoriserEtPlanifier(
            @PathVariable Long id,
            @RequestBody PlanificationDTO dto) {

        DemandeSoutenance demande = soutenanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (demande.getStatut() != DemandeSoutenance.StatutSoutenance.JURY_PROPOSE) {
            return ResponseEntity.badRequest().body("Le jury doit être proposé par le directeur avant la planification.");
        }

        // Extraction des données du DTO et parsing de la date
        try {
            demande.setDateSoutenance(LocalDateTime.parse(dto.getDate()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Format de date invalide. Utilisez le format ISO (ex: 2026-06-15T10:00:00)");
        }

        demande.setHeure(dto.getHeure());
        demande.setLieu(dto.getLieu());
        demande.setStatut(DemandeSoutenance.StatutSoutenance.AUTORISEE_ET_PLANIFIEE);

        soutenanceRepo.save(demande);

        return ResponseEntity.ok("Soutenance officiellement autorisée, planifiée et communiquée.");
    }

    @GetMapping("/directeur/mes-demandes")
    @PreAuthorize("hasAuthority('ROLE_DIRECTEUR')")
    public ResponseEntity<?> getDemandesPourDirecteur(Principal principal) {
        Utilisateur user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Directeur non trouvé"));

        List<DemandeSoutenance> demandes = soutenanceRepo.findByDirecteurAndStatut(
                (DirecteurThese) user,
                DemandeSoutenance.StatutSoutenance.SOUMISE_EN_ATTENTE_JURY
        );
        return ResponseEntity.ok(demandes);
    }
    @GetMapping("/mon-suivi")
    @PreAuthorize("hasAuthority('ROLE_CANDIDAT')")
    public ResponseEntity<?> obtenirMonSuivi(Principal principal) {
        Utilisateur user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!(user instanceof CandidatDoctorant)) {
            return ResponseEntity.badRequest().body("Action réservée aux candidats doctorants.");
        }
        CandidatDoctorant candidat = (CandidatDoctorant) user;

        try {
            SoutenanceStatusDTO suivi = soutenanceService.obtenirSuiviCandidat(candidat);
            return ResponseEntity.ok(suivi);
        } catch (RuntimeException e) {
            // Renvoie un message propre si aucune demande n'a encore été soumise
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}