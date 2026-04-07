package com.example.fiducia.services;

import com.example.fiducia.dto.DossierStatusDTO;
import com.example.fiducia.entities.CandidatDoctorant;
import com.example.fiducia.entities.DossierInscription;
import com.example.fiducia.entities.StatutDossier;
import com.example.fiducia.repositories.DossierInscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DossierInscriptionService {

    @Autowired
    private DossierInscriptionRepository dossierRepository;

    @Transactional
    public void changerStatut(Long dossierId, StatutDossier nouveauStatut) { // Changé String en StatutDossier
        DossierInscription dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        dossier.setStatut(nouveauStatut);
        dossier.setDateDerniereModification(LocalDateTime.now());
        dossierRepository.save(dossier);
    }

    public DossierStatusDTO obtenirSuiviCandidat(CandidatDoctorant candidat) {
        DossierInscription dossier = dossierRepository.findByCandidat(candidat)
                .orElseThrow(() -> new RuntimeException("Aucun dossier trouvé"));

        DossierStatusDTO dto = new DossierStatusDTO();
        dto.setId(dossier.getId());
        dto.setSujetThèse(dossier.getSujetThèse());
        dto.setNomDirecteur(dossier.getDirecteurThese().getNom() + " " + dossier.getDirecteurThese().getPrenom());

        // Correction : On convertit l'Enum en String pour le DTO
        dto.setStatut(dossier.getStatut().name());

        dto.setDerniereModification(dossier.getDateDerniereModification());

        // Correction du Switch : On utilise les valeurs de l'Enum, pas des Strings entre guillemets
        switch (dossier.getStatut()) {
            case SOUMIS -> dto.setMessageEtape("Votre dossier a été transmis.");
            case EN_ATTENTE_DIRECTEUR -> dto.setMessageEtape("Votre dossier est chez le directeur pour avis.");
            case VALIDE -> dto.setMessageEtape("Félicitations ! Votre inscription est validée.");
            case REJETE -> dto.setMessageEtape("Dossier refusé.");
            default -> dto.setMessageEtape("Dossier en cours de traitement.");
        }

        return dto;
    }
}