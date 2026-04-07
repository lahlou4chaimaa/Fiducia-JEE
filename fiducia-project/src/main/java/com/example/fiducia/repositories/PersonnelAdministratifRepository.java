package com.example.fiducia.repositories;

import com.example.fiducia.entities.PersonnelAdministratif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelAdministratifRepository extends JpaRepository<PersonnelAdministratif, Long> {
}