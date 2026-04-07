package com.example.fiducia.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class DossierStatusDTO {
    private Long id;
    private String sujetThèse;
    private String nomDirecteur;
    private String statut;
    private LocalDateTime derniereModification;
    private String messageEtape; // Un message explicatif pour l'utilisateur
}