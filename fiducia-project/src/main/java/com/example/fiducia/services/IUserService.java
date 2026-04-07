package com.example.fiducia.services;

import com.example.fiducia.entities.*;
import com.example.fiducia.repositories.RoleRepository;
import com.example.fiducia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// IUserService.java
public interface IUserService {
    void registerCandidat(CandidatDoctorant doctorant);
    void registerDirecteur(DirecteurThese directeur);
    void registerAdmin(PersonnelAdministratif admin);
    Utilisateur authenticate(String email, String password);
    List<Utilisateur> getAllUsers();
    void addRoleToUser(Long userId, String roleName);
    void removeRoleFromUser(Long userId, String roleName);
}

