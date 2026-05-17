package com.example.fiducia.controllers;

import com.example.fiducia.entities.*;
import com.example.fiducia.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Correction de l'import ici
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.security.Principal;

@Controller
public class ViewController {

    @Autowired private UserRepository userRepository;
    @Autowired private CampagneRepository campagneRepository;
    @Autowired private DirecteurTheseRepository directeurRepository;
    @Autowired private DossierInscriptionRepository dossierRepo;
    @Autowired private DemandeSoutenanceRepository soutenanceRepo;

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
        List<DemandeSoutenance> demandesJury = soutenanceRepo.findByStatut(DemandeSoutenance.StatutSoutenance.JURY_PROPOSE);
        model.addAttribute("demandesJuryProposes", demandesJury);
        return "admin/dashboard";
    }

    @GetMapping("/profile")
    public String profilePage() {
        return "profil"; // correspond à profil.html dans templates
    }

    @GetMapping("/candidat/dashboard")
    public String candidatDashboard() {
        /*Utilisateur user = userRepository.findByEmail(principal.getName()).orElseThrow();
        if (user instanceof CandidatDoctorant) {
            Optional<DemandeSoutenance> demandeOpt = soutenanceRepo.findByDoctorant((CandidatDoctorant) user);
            if (demandeOpt.isPresent()) {
                DemandeSoutenance d = demandeOpt.get();
                model.addAttribute("demandeSoutenance", d);

                // Génération du message d'étape personnalisé
                String message = switch (d.getStatut()) {
                    case SOUMISE_EN_ATTENTE_JURY -> "Vos prérequis sont validés. Votre dossier est en cours d'examen par votre Directeur de thèse pour la proposition du jury.";
                    case JURY_PROPOSE -> "Le jury a été proposé par votre directeur. L'administration étudie les rapports pour valider définitivement la date.";
                    case AUTORISEE_ET_PLANIFIEE -> "Félicitations ! Votre soutenance est officiellement autorisée et planifiée.";
                    default -> "Statut inconnu.";
                };
                model.addAttribute("messageEtape", message);
            } else {
                model.addAttribute("demandeSoutenance", null);
            }
        }*/

        return "candidat/dashboard"; // templates/candidat/dashboard.html
    }

    @GetMapping("/directeur/dashboard")
    public String directeurDashboard() {
        /*Utilisateur user = userRepository.findByEmail(principal.getName()).orElseThrow();
        if (user instanceof DirecteurThese) {
            List<DemandeSoutenance> demandes = soutenanceRepo.findByDirecteurAndStatut(
                    (DirecteurThese) user, DemandeSoutenance.StatutSoutenance.SOUMISE_EN_ATTENTE_JURY);
            model.addAttribute("demandesSoutenance", demandes);
        }*/
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

    @GetMapping("/directeur/soutenances")
    public String directeurSoutenances() {
        return "directeur/soutenance-directeur";
    }

    @GetMapping("/candidat/soutenance")
    public String candidatSoutenances() {
        return "candidat/soutenance-candidat";
    }

    /*@GetMapping("/")
    public String index() {
        return "redirect:/login";
    }*/

    @GetMapping("/")
    public String index() {
        return "welcome"; // correspond à src/main/resources/templates/welcome.html
    }
}