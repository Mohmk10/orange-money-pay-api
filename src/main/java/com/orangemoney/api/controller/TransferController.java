package com.orangemoney.api.controller;

import com.orangemoney.api.dto.request.TransferRequest;
import com.orangemoney.api.dto.response.TransferResponse;
import com.orangemoney.api.security.UserPrincipal;
import com.orangemoney.api.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transfers")
@Tag(name = "Transfers", description = "Gestion des transferts d'argent")
@SecurityRequirement(name = "bearerAuth")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/initiate")
    @Operation(summary = "Initier un transfert", description = "Envoyer de l'argent à un autre utilisateur")
    public ResponseEntity<TransferResponse> initiateTransfer(@Valid @RequestBody TransferRequest request) {
        Long senderId = UserPrincipal.getCurrentUserId();
        TransferResponse response = transferService.initiateTransfer(senderId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    @Operation(summary = "Historique des transferts", description = "Consulter l'historique complet des transferts")
    public ResponseEntity<List<TransferResponse>> getHistory() {
        Long userId = UserPrincipal.getCurrentUserId();
        List<TransferResponse> history = transferService.getTransferHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{reference}")
    @Operation(summary = "Détails d'un transfert", description = "Récupérer les détails d'un transfert par référence")
    public ResponseEntity<TransferResponse> getTransferDetails(@PathVariable String reference) {
        TransferResponse response = transferService.getTransferByReference(reference);
        return ResponseEntity.ok(response);
    }
}