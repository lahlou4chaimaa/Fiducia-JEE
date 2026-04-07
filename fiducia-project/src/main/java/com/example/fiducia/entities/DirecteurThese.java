package com.example.fiducia.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DirecteurThese extends Utilisateur {
    private String grade; // ex: Professeur de l'Enseignement Supérieur (PES), PH...
    private String laboratoire;
    private String specialite;
    private boolean habilitéDirigerRecherche; // HDR
}