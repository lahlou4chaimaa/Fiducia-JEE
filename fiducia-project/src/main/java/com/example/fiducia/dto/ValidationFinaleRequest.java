package com.example.fiducia.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ValidationFinaleRequest {
    private boolean valide;
    private String remarques;

}
