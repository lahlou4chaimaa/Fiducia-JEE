package com.example.fiducia.services;

import com.example.fiducia.dto.SoutenanceStatusDTO;
import com.example.fiducia.entities.*;
import com.example.fiducia.repositories.DemandeSoutenanceRepository;
import com.example.fiducia.repositories.DossierInscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoutenanceService {

    @Autowired private DemandeSoutenanceRepository soutenanceRepo;
    @Autowired private DossierInscriptionRepository dossierRepo;

    public void verifierEligibiliteSoutenance(CandidatDoctorant candidat, int articles, int confs, int heures) {
        // 1. Vérification de l'inscription validée
        DossierInscription dossier = dossierRepo.findByCandidat(candidat)
                .orElseThrow(() -> new RuntimeException("Aucun dossier d'inscription trouvé pour ce candidat."));

        if (dossier.getStatut() != StatutDossier.VALIDE) {
            throw new RuntimeException("Impossible de demander une soutenance : Votre dossier d'inscription n'est pas VALIDE.");
        }

        // 2. Vérification des prérequis scientifiques et de formation
        if (articles < 2) {
            throw new RuntimeException("Prérequis insuffisant : Au moins 2 articles journaux (Q1/Q2) sont requis.");
        }
        if (confs < 2) {
            throw new RuntimeException("Prérequis insuffisant : Au moins 2 conférences (ou équivalent) sont requises.");
        }
        if (heures < 200) {
            throw new RuntimeException("Prérequis insuffisant : Vous devez compléter au moins 200 heures de formation.");
        }
    }

    public DemandeSoutenance creerDemande(DemandeSoutenance demande) {
        demande.setStatut(DemandeSoutenance.StatutSoutenance.SOUMISE_EN_ATTENTE_JURY);
        return soutenanceRepo.save(demande);
    }
    // Dans SoutenanceService.java

    public SoutenanceStatusDTO obtenirSuiviCandidat(CandidatDoctorant candidat) {
        // On cherche la demande de soutenance associée au candidat
        DemandeSoutenance demande = soutenanceRepo.findByDoctorant(candidat)
                .orElseThrow(() -> new RuntimeException("Aucune demande de soutenance trouvée pour ce candidat."));

        SoutenanceStatusDTO dto = new SoutenanceStatusDTO();
        dto.setId(demande.getId());
        dto.setStatut(demande.getStatut().name());
        dto.setNbArticlesQ1Q2(demande.getNbArticlesQ1Q2());
        dto.setNbConferences(demande.getNbConferences());
        dto.setHeuresFormation(demande.getHeuresFormationCompletees());

        // Switch sur le statut pour donner un message clair à l'interface utilisateur
        switch (demande.getStatut()) {
            case SOUMISE_EN_ATTENTE_JURY -> {
                dto.setMessageEtape("Vos prérequis sont validés. Votre dossier est en cours d'examen par votre Directeur de thèse pour la proposition du jury.");
            }
            case JURY_PROPOSE -> {
                dto.setMessageEtape("Le jury a été proposé par votre directeur. L'administration étudie les rapports pour valider définitivement votre soutenance.");
            }
            case AUTORISEE_ET_PLANIFIEE -> {
                dto.setMessageEtape("Félicitations ! Votre soutenance est officiellement autorisée.");
                // On ne donne les détails de planification que lorsque c'est validé
                dto.setDateSoutenance(demande.getDateSoutenance());
                dto.setHeure(demande.getHeure());
                dto.setLieu(demande.getLieu());
            }
            default -> dto.setMessageEtape("Statut de la demande inconnu ou en cours de traitement.");
        }

        return dto;
    }
}