package com.example.fiducia.repositories;

import com.example.fiducia.entities.Campagne;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CampagneRepository extends JpaRepository<Campagne, Long> {
    // Trouver la campagne active actuelle
    Optional<Campagne> findByActiveTrue();

}