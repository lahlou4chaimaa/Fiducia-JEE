package com.example.fiducia.controllers;

import com.example.fiducia.entities.Campagne;
import com.example.fiducia.entities.StatutDossier;
import com.example.fiducia.repositories.CampagneRepository;
import com.example.fiducia.repositories.DirecteurTheseRepository;
import com.example.fiducia.repositories.DossierInscriptionRepository;
import com.example.fiducia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Correction de l'import ici
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewController {

    @Autowired private UserRepository userRepository;
    @Autowired private CampagneRepository campagneRepository;
    @Autowired private DirecteurTheseRepository directeurRepository;
    @Autowired private DossierInscriptionRepository dossierRepo;

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "auth/register";
    }

    // Gestion des campagnes dans le layout
    @GetMapping("/admin/campagnes")
    public String viewCampagnes(Model model) {
        model.addAttribute("campagnes", campagneRepository.findAll());
        model.addAttribute("allDirecteurs", directeurRepository.findAll());
        return "admin/campagnes"; // Assure-toi que ce fichier utilise th:replace ou th:include
    }

    @GetMapping("/admin/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        // Il faut impérativement charger les données ici aussi !
        model.addAttribute("campagnes", campagneRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("allDirecteurs", directeurRepository.findAll());
        model.addAttribute("dossiersEnAttente", dossierRepo.findByStatut(StatutDossier.EN_ATTENTE_ADMIN));
        return "admin/dashboard";
    }
    @GetMapping("/profile")
    public String profilePage() {
        return "profil"; // correspond à profil.html dans templates
    }
    @GetMapping("/candidat/dashboard")
    public String candidatDashboard() {
        return "candidat/dashboard"; // templates/candidat/dashboard.html
    }
    @GetMapping("/directeur/dashboard")
    public String directeurDashboard() {
        return "directeur/dashboard";
    }

    @GetMapping("/candidat/soumission")
    public String soumissionPage(Model model) {
        // On récupère TOUTES les campagnes via le repository standard
        List<Campagne> toutesLesCampagnes = campagneRepository.findAll();

        // On filtre manuellement pour n'avoir que les actives (si tu ne veux pas changer le repo)
        List<Campagne> activeCampagnes = toutesLesCampagnes.stream()
                .filter(Campagne::isActive) // Vérifie que ta variable s'appelle bien 'active' dans l'entité
                .toList();

        // On passe la liste filtrée au modèle
        model.addAttribute("activeCampagnes", activeCampagnes);

        return "candidat/soumission";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
}