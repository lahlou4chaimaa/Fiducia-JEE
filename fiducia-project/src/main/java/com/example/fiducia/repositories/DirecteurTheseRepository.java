package com.example.fiducia.repositories;

import com.example.fiducia.entities.DirecteurThese;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirecteurTheseRepository extends JpaRepository<DirecteurThese, Long> {
    // Tu peux ajouter des méthodes spécifiques plus tard,
    // comme trouver les directeurs par laboratoire.
}