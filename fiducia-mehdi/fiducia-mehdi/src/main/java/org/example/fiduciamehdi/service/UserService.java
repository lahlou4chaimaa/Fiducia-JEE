package org.example.fiduciamehdi.service;

import org.example.fiduciamehdi.DTO.RegisterRequest;
import org.example.fiduciamehdi.entity.Role;
import org.example.fiduciamehdi.entity.User;
import org.example.fiduciamehdi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email)) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        User u = new User();
        u.setFullName(req.fullName);
        u.setEmail(req.email);
        u.setPassword(passwordEncoder.encode(req.password));
        if (req.email.equals("admin@fideucia.ma")) {
            u.setRole(Role.SUPER_ADMIN);//temporairement
        } else {
            u.setRole(Role.DOCTORAT);
        }  // ← CHANGÉ : DOCTORANT par défaut

        return userRepository.save(u);
    }
}