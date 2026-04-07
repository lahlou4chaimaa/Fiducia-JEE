package org.example.fiduciamehdi.controller;

import org.example.fiduciamehdi.entity.User;
import org.example.fiduciamehdi.repository.UserRepository;
import org.example.fiduciamehdi.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("") // ✅ Fix : chemin exact
    public String adminDashboard(Model model, Authentication auth) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", Role.values());
        return "admin/dashboard"; // ✅ Correspond au template
    }

    @PostMapping("/update-role/{id}")
    public String updateRole(@PathVariable Long id, @RequestParam Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(role);
        userRepository.save(user);
        return "redirect:/admin";
    }
}