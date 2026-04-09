package com.example.fiducia.controllers;

import com.example.fiducia.config.JwtUtils;
import com.example.fiducia.dto.JwtResponse;
import com.example.fiducia.dto.LoginRequest;
import com.example.fiducia.dto.RegistrationRequest;
import com.example.fiducia.entities.CandidatDoctorant;
import com.example.fiducia.entities.DirecteurThese;
import com.example.fiducia.entities.PersonnelAdministratif;
import com.example.fiducia.entities.Utilisateur;
import com.example.fiducia.repositories.UserRepository;
import com.example.fiducia.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private IUserService userService; // Utilise l'interface ici !
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        // On prépare l'objet
        CandidatDoctorant doctorant = new CandidatDoctorant();
        doctorant.setEmail(req.getEmail());
        doctorant.setNom(req.getNom());
        doctorant.setPrenom(req.getPrenom());
        doctorant.setPassword(req.getPassword()); // On passe le mot de passe en clair au service

        // ON APPELLE LE SERVICE ! (C'est lui qui va hacher et ajouter le rôle)
        userService.registerCandidat(doctorant);

        return ResponseEntity.ok("Compte candidat créé avec succès");
    }
    // 2. Inscription pour les DIRECTEURS (Sera protégé par ROLE_ADMIN plus tard)
    @PostMapping("/register-directeur")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerDirecteur(@RequestBody DirecteurThese directeur) {
        if (userRepository.existsByEmail(directeur.getEmail())) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }
        userService.registerDirecteur(directeur);
        return ResponseEntity.ok("Compte Directeur créé avec succès");
    }

    // 3. Inscription pour le PERSONNEL ADMINISTRATIF
    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody PersonnelAdministratif admin) {
        if (userRepository.existsByEmail(admin.getEmail())) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }
        userService.registerAdmin(admin);
        return ResponseEntity.ok("Compte Administratif créé avec succès");
    }

    // Dans AuthController.java
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        // On appelle le service au lieu de tout faire ici
        Utilisateur user = userService.authenticate(req.getEmail(), req.getPassword());

        String jwt = jwtUtils.generateJwtToken(user.getEmail());
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getLibelle())
                .toList();

        return ResponseEntity.ok(new JwtResponse(jwt, user.getEmail(), roles));
    }
    @GetMapping("/me/profile")
    public ResponseEntity<?> getMyProfile(Principal principal) {
        Utilisateur user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return ResponseEntity.ok(user);
    }
    @PutMapping("/me/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> updates, Principal principal) {
        Utilisateur user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 1. CHAMPS COMMUNS (Utilisateur)
        if(updates.containsKey("nom")) user.setNom((String) updates.get("nom"));
        if(updates.containsKey("prenom")) user.setPrenom((String) updates.get("prenom"));

        // 2. CAS : DIRECTEUR DE THÈSE
        if (user instanceof DirecteurThese dt) {
            if(updates.containsKey("grade")) dt.setGrade((String) updates.get("grade"));
            if(updates.containsKey("laboratoire")) dt.setLaboratoire((String) updates.get("laboratoire"));
            if(updates.containsKey("specialite")) dt.setSpecialite((String) updates.get("specialite"));
            if(updates.containsKey("habilitéDirigerRecherche")) {
                dt.setHabilitéDirigerRecherche((boolean) updates.get("habilitéDirigerRecherche"));
            }
        }

        // 3. CAS : CANDIDAT DOCTORANT
        else if (user instanceof CandidatDoctorant cd) {
            if(updates.containsKey("totalCreditsCumules")) {
                Object val = updates.get("totalCreditsCumules");
                if (val instanceof Number n) cd.setTotalCreditsCumules(n.doubleValue());
            }
        }

        // 4. CAS : PERSONNEL ADMINISTRATIF
        else if (user instanceof PersonnelAdministratif pa) {
            if(updates.containsKey("service")) pa.setService((String) updates.get("service"));
            if(updates.containsKey("poste")) pa.setPoste((String) updates.get("poste"));
        }

        userRepository.save(user);
        return ResponseEntity.ok(user); // Retourne l'objet complet mis à jour
    }
}