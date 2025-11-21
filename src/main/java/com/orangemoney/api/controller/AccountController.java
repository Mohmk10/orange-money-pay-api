package com.orangemoney.api.controller;

import com.orangemoney.api.dto.request.UpdateProfileRequest;
import com.orangemoney.api.dto.response.AccountResponse;
import com.orangemoney.api.dto.response.BalanceResponse;
import com.orangemoney.api.dto.response.UserResponse;
import com.orangemoney.api.security.UserPrincipal;
import com.orangemoney.api.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Account", description = "Gestion du compte utilisateur")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/profile")
    @Operation(summary = "Profil utilisateur", description = "Récupérer le profil complet de l'utilisateur connecté")
    public ResponseEntity<UserResponse> getProfile() {
        Long userId = UserPrincipal.getCurrentUserId();
        UserResponse response = accountService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    @Operation(summary = "Modifier profil", description = "Mettre à jour les informations du profil")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        Long userId = UserPrincipal.getCurrentUserId();
        UserResponse response = accountService.updateProfile(
                userId,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details")
    @Operation(summary = "Détails du compte", description = "Récupérer les détails du compte Orange Money")
    public ResponseEntity<AccountResponse> getAccountDetails() {
        Long userId = UserPrincipal.getCurrentUserId();
        AccountResponse response = accountService.getAccountDetails(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance")
    @Operation(summary = "Consulter solde", description = "Consulter le solde du compte")
    public ResponseEntity<BalanceResponse> getBalance() {
        Long userId = UserPrincipal.getCurrentUserId();
        BigDecimal balance = accountService.getBalance(userId);
        return ResponseEntity.ok(new BalanceResponse(balance));
    }
}