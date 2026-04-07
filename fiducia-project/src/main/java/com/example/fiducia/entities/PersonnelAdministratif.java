package com.example.fiducia.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PersonnelAdministratif extends Utilisateur {
    private String service; // ex: Service Doctorat, Scolarité
    private String poste;   // ex: Chef de service, Agent administratif
}