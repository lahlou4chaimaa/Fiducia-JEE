package com.example.fiducia.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class SoutenanceStatusDTO {
    private Long id;
    private String statut;
    private String messageEtape;
    private int nbArticlesQ1Q2;
    private int nbConferences;
    private int heuresFormation;

    // Informations de planification (visibles uniquement si validées)
    private LocalDateTime dateSoutenance;
    private String heure;
    private String lieu;
}