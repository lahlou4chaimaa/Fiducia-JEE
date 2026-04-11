package com.example.fiducia.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class DossierInscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "candidat_id")
    private CandidatDoctorant candidat;

    @ManyToOne
    @JoinColumn(name = "directeur_id")
    private DirecteurThese directeurThese;

    private String sujetThèse;
    private String laboratoire;

    // Chemins des fichiers (On stocke le nom du fichier sur le disque)
    private String cvPath;
    private String diplomePath;
    private String lettreMotivationPath;
    private String collaboration;

    // Statuts : BROUILLON, SOUMIS, EN_ATTENTE_DIRECTEUR, EN_ATTENTE_ADMIN, VALIDE, REJETE
    @Enumerated(EnumType.STRING)
    private StatutDossier statut = StatutDossier.BROUILLON;

    private LocalDateTime dateCreation = LocalDateTime.now();
    private LocalDateTime dateDerniereModification;

    private String avisDirecteur; // "FAVORABLE" ou "DEFAVORABLE"
    private String commentaireDirecteur;
    private String remarquesAdmin;

    @ManyToOne
    @JoinColumn(name = "campagne_id")
    private Campagne campagne;
}