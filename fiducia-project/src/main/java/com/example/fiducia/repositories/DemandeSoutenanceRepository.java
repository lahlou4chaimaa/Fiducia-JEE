package com.example.fiducia.repositories;

import com.example.fiducia.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeSoutenanceRepository extends JpaRepository<DemandeSoutenance, Long> {

    Optional<DemandeSoutenance> findByDoctorant(CandidatDoctorant doctorant);

    List<DemandeSoutenance> findByDirecteurAndStatut(
            DirecteurThese directeur,
            DemandeSoutenance.StatutSoutenance statut
    );

    List<DemandeSoutenance> findByStatut(DemandeSoutenance.StatutSoutenance statut);
}