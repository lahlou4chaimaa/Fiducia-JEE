package org.example.fiduciamehdi.controller;
import org.example.fiduciamehdi.DTO.LoginRequest;
import org.example.fiduciamehdi.DTO.RegisterRequest;
import org.example.fiduciamehdi.entity.User;
import org.example.fiduciamehdi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest req) {
        try {
            User saved = userService.register(req);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Compte créé avec succès");
            response.put("userId", saved.getId());
            return ResponseEntity.ok(response); // ✅ JSON
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(409).body(error);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email, req.password)
            );
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login OK");
            response.put("email", auth.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Email ou mot de passe incorrect");
            return ResponseEntity.status(401).body(error);
        }
    }
}