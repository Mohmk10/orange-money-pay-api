package com.orangemoney.api.controller;

import com.orangemoney.api.dto.request.LoginRequest;
import com.orangemoney.api.dto.request.RegisterRequest;
import com.orangemoney.api.dto.response.AuthResponse;
import com.orangemoney.api.dto.response.UserResponse;
import com.orangemoney.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints d'authentification")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Inscription", description = "Créer un nouveau compte utilisateur")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion", description = "Se connecter avec numéro et mot de passe")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    @Operation(summary = "Vérifier email", description = "Vérifier le compte avec le token reçu par email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Compte vérifié avec succès");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Renvoyer email", description = "Renvoyer l'email de vérification")
    public ResponseEntity<Map<String, String>> resendVerification(@RequestParam Long userId) {
        authService.resendVerificationEmail(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email de vérification renvoyé");
        return ResponseEntity.ok(response);
    }
}