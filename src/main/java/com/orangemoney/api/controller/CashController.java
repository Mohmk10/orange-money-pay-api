package com.orangemoney.api.controller;

import com.orangemoney.api.dto.request.DepositRequest;
import com.orangemoney.api.dto.request.WithdrawalRequest;
import com.orangemoney.api.dto.response.CashTransactionResponse;
import com.orangemoney.api.security.UserPrincipal;
import com.orangemoney.api.service.CashService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cash")
@Tag(name = "Cash", description = "Dépôts et retraits d'argent")
@SecurityRequirement(name = "bearerAuth")
public class CashController {

    private final CashService cashService;

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    @PostMapping("/deposit")
    @Operation(summary = "Demander un dépôt", description = "Déposer de l'argent via un agent")
    public ResponseEntity<CashTransactionResponse> requestDeposit(@Valid @RequestBody DepositRequest request) {
        Long userId = UserPrincipal.getCurrentUserId();
        CashTransactionResponse response = cashService.requestDeposit(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/withdrawal")
    @Operation(summary = "Initier un retrait", description = "Retirer de l'argent chez un agent")
    public ResponseEntity<CashTransactionResponse> initiateWithdrawal(@Valid @RequestBody WithdrawalRequest request) {
        Long userId = UserPrincipal.getCurrentUserId();
        CashTransactionResponse response = cashService.initiateWithdrawal(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    @Operation(summary = "Historique cash", description = "Consulter l'historique des dépôts et retraits")
    public ResponseEntity<List<CashTransactionResponse>> getHistory() {
        Long userId = UserPrincipal.getCurrentUserId();
        List<CashTransactionResponse> history = cashService.getCashHistory(userId);
        return ResponseEntity.ok(history);
    }
}