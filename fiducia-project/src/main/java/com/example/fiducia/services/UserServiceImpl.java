package com.example.fiducia.services;

import com.example.fiducia.entities.*;
import com.example.fiducia.repositories.RoleRepository;
import com.example.fiducia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

// UserServiceImpl.java
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder encoder;

    @Override
    public void registerCandidat(CandidatDoctorant doctorant) {
        // Hachage
        doctorant.setPassword(encoder.encode(doctorant.getPassword()));

        // Sécurité : s'assurer que le Set de rôles existe
        if (doctorant.getRoles() == null) {
            doctorant.setRoles(new HashSet<>());
        }

        // Attribution du rôle par défaut
        Role userRole = roleRepository.findByLibelle("ROLE_CANDIDAT") // Change ici si besoin
                .orElseThrow(() -> new RuntimeException("Erreur: Rôle ROLE_CANDIDAT non trouvé en base."));

        doctorant.getRoles().add(userRole);

        userRepository.save(doctorant);
    }

    @Override
    public Utilisateur authenticate(String email, String password) {
        Utilisateur user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (encoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Identifiants incorrects");
    }
    @Override
    public List<Utilisateur> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional // Important pour la modification en base
    public void addRoleToUser(Long userId, String roleName) {
        Utilisateur user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Role role = roleRepository.findByLibelle(roleName)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

        user.getRoles().add(role);
        // Pas besoin de .save() si @Transactional est utilisé, Hibernate synchronise tout seul
    }
    @Override
    @Transactional // Indispensable pour que Hibernate génère le DELETE dans user_roles
    public void removeRoleFromUser(Long userId, String roleName) {
        Utilisateur user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // On retire le rôle de la liste si le libellé correspond
        boolean removed = user.getRoles().removeIf(role -> role.getLibelle().equals(roleName));

        if (!removed) {
            throw new RuntimeException("L'utilisateur ne possède pas le rôle : " + roleName);
        }


        // Hibernate détecte que la collection a rétréci et supprime la ligne en base
    }
    @Override
    @Transactional
    public void registerDirecteur(DirecteurThese directeur) {
        processRegistration(directeur, "ROLE_DIRECTEUR");
    }

    @Override
    @Transactional
    public void registerAdmin(PersonnelAdministratif admin) {
        processRegistration(admin, "ROLE_ADMIN");
    }
    private void processRegistration(Utilisateur user, String roleLibelle) {
        // 1. Hachage du mot de passe
        user.setPassword(encoder.encode(user.getPassword()));

        // 2. Initialisation et attribution du rôle
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        Role role = roleRepository.findByLibelle(roleLibelle)
                .orElseThrow(() -> new RuntimeException("Erreur: Rôle " + roleLibelle + " non trouvé."));

        user.getRoles().add(role);

        // 3. Sauvegarde polymorphique (remplit les tables parent/fille)
        userRepository.save(user);
    }
}
