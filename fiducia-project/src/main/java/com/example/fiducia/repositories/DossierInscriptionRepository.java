package com.example.fiducia.repositories;

import com.example.fiducia.entities.CandidatDoctorant;
import com.example.fiducia.entities.DossierInscription;
import com.example.fiducia.entities.DirecteurThese;
import com.example.fiducia.entities.StatutDossier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DossierInscriptionRepository extends JpaRepository<DossierInscription, Long> {
    Optional<DossierInscription> findByCandidat(CandidatDoctorant candidat);

    // Change String statut en StatutDossier statut
    List<DossierInscription> findByDirecteurTheseAndStatut(DirecteurThese directeur, StatutDossier statut);
    List<DossierInscription> findByStatut(StatutDossier statut);
}