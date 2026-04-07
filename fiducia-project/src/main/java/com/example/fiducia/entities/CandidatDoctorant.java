package com.example.fiducia.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

// CandidatDoctorant.java
@Entity
@Getter
@Setter
public class CandidatDoctorant extends Utilisateur {
    private Double totalCreditsCumules;
}
