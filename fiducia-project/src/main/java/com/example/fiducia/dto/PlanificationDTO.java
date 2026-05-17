package com.example.fiducia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanificationDTO {
    private String date; // Format: "2026-06-15T10:00:00"
    private String heure;
    private String lieu;
}