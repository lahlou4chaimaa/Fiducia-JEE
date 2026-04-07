package com.example.fiducia.controllers;

import com.example.fiducia.dto.RoleRequest;
import com.example.fiducia.entities.Campagne;
import com.example.fiducia.entities.Utilisateur;
import com.example.fiducia.repositories.CampagneRepository;
import com.example.fiducia.repositories.UserRepository;
import com.example.fiducia.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IUserService userService;
    @Autowired private CampagneRepository campagneRepository;
    @Autowired private UserRepository userRepository;

    // Voir tous les utilisateurs
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<Utilisateur>> getAllUsers() {
        // Grâce au polymorphisme, cela retourne tous les types d'utilisateurs
        return ResponseEntity.ok(userRepository.findAll());
    }
    // Ajouter un rôle
    @PostMapping("/users/{id}/add-role")
    public ResponseEntity<?> addRole(@PathVariable Long id, @RequestBody RoleRequest request) {
        try {
            userService.addRoleToUser(id, request.getRoleName());
            return ResponseEntity.ok("Rôle ajouté avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Retirer un rôle
    @PostMapping("/users/{id}/remove-role") // Note: On peut garder POST ou utiliser PATCH
    public ResponseEntity<?> removeRole(@PathVariable Long id, @RequestBody RoleRequest request) {
        try {
            userService.removeRoleFromUser(id, request.getRoleName());
            return ResponseEntity.ok("Rôle retiré avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/campagnes")
    @Transactional
    public ResponseEntity<?> creerCampagne(@RequestBody Campagne campagne) {
        // On s'assure qu'une seule campagne est active à la fois
        if (campagne.isActive()) {
            campagneRepository.findByActiveTrue().ifPresent(c -> {
                c.setActive(false);
                campagneRepository.save(c);
            });
        }
        campagneRepository.save(campagne);
        return ResponseEntity.ok("Campagne créée avec succès !");
    }
    @GetMapping("/campagnes")
    public ResponseEntity<List<Campagne>> getAllCampagnes() {
        return ResponseEntity.ok(campagneRepository.findAll());
    }

    @GetMapping("/campagnes/active")
    public ResponseEntity<?> getCampagneActive() {
        return campagneRepository.findByActiveTrue()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PatchMapping("/campagnes/{id}/desactiver")
    public ResponseEntity<?> desactiverCampagne(@PathVariable Long id) {
        Campagne campagne = campagneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campagne non trouvée"));
        campagne.setActive(false);
        campagneRepository.save(campagne);
        return ResponseEntity.ok("La campagne " + campagne.getLibelle() + " est désormais fermée.");
    }

}