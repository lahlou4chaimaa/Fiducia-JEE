package com.example.fiducia.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AvisRequest {
    private boolean valide;
    private String commentaires;
    // Getters et Setters
}