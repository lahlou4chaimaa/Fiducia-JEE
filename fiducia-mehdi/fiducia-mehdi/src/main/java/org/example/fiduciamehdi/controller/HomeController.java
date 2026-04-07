package org.example.fiduciamehdi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;  // ✅ OBLIGATOIRE
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.stream.Collectors;  // ✅ OBLIGATOIRE

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            String roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            System.out.println("🔍 User: " + auth.getName() + " | Roles: " + roles);

            if (roles.contains("ROLE_SUPER_ADMIN")) {
                return "redirect:/admin";
            }
            model.addAttribute("user", auth.getName());
        }
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
