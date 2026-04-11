package com.example.fiducia.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Campagne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle; // ex: "Session Automne 2026"
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private boolean active = true;

    // Dans Campagne.java
    @ManyToMany
    @JoinTable(
            name = "campagne_directeurs",
            joinColumns = @JoinColumn(name = "campagne_id"),
            inverseJoinColumns = @JoinColumn(name = "directeur_id")
    )
    private List<DirecteurThese> directeursAssocies;

    // Méthode utilitaire pour savoir si on est dans les dates
    public boolean isOuverte() {
        LocalDate today = LocalDate.now();
        return active && !today.isBefore(dateDebut) && !today.isAfter(dateFin);
    }
}