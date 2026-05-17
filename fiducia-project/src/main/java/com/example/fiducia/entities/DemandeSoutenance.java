package com.example.fiducia.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class DemandeSoutenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctorant_id", unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CandidatDoctorant doctorant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directeur_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DirecteurThese directeur;

    // --- Nouveaux Prérequis Stricts ---
    private int nbArticlesQ1Q2;
    private int nbConferences;
    private int heuresFormationCompletees; // Doit être >= 200

    // --- Chemins des Documents Requis ---
    private String demandeManuscritePath;
    private String rapportThesePath;
    private String rapportAntiPlagiatPath;
    private String rapportPublicationsCommunicationsPath;
    private String copiesAttestationsFormationsPath;

    // --- Jury (Proposé par le Directeur) ---
    @ElementCollection
    private List<String> membresJuryProposes;

    // --- Planification Administrative ---
    private LocalDateTime dateSoutenance;
    private String lieu;
    private String heure;

    @Enumerated(EnumType.STRING)
    private StatutSoutenance statut;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    public enum StatutSoutenance {
        SOUMISE_EN_ATTENTE_JURY,   // Étape 1 : Le doctorant a soumis, attend que le Directeur propose le jury
        JURY_PROPOSE,              // Étape 2 : Le directeur a proposé le jury, attend les rapports favorables / validation administrative
        AUTORISEE_ET_PLANIFIEE     // Étape 3 : Rapports reçus, l'administration a planifié et validé
    }
}