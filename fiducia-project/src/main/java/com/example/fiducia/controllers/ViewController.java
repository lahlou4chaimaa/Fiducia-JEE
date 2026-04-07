package com.example.fiducia.controllers;

import com.example.fiducia.repositories.CampagneRepository;
import com.example.fiducia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Correction de l'import ici
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Autowired private UserRepository userRepository;
    @Autowired private CampagneRepository campagneRepository;

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
        return "admin/campagnes"; // Assure-toi que ce fichier utilise th:replace ou th:include
    }

    @GetMapping("/admin/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }
    @GetMapping("/admin/dashboard")

    public String showAdminDashboard() {

        return "admin/dashboard";

    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
}