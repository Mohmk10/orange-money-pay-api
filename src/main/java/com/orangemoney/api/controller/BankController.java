package com.orangemoney.api.controller;

import com.orangemoney.api.dto.request.BankTransferRequest;
import com.orangemoney.api.dto.request.LinkBankAccountRequest;
import com.orangemoney.api.dto.response.BankTransferResponse;
import com.orangemoney.api.dto.response.LinkedBankAccountResponse;
import com.orangemoney.api.security.UserPrincipal;
import com.orangemoney.api.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bank")
@Tag(name = "Bank", description = "Liaison et transferts avec comptes bancaires")
@SecurityRequirement(name = "bearerAuth")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/link")
    @Operation(summary = "Lier un compte bancaire", description = "Lier un compte bancaire à votre compte Orange Money")
    public ResponseEntity<LinkedBankAccountResponse> linkAccount(@Valid @RequestBody LinkBankAccountRequest request) {
        Long userId = UserPrincipal.getCurrentUserId();
        LinkedBankAccountResponse response = bankService.linkBankAccount(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    @Operation(summary = "Comptes bancaires liés", description = "Consulter tous vos comptes bancaires liés")
    public ResponseEntity<List<LinkedBankAccountResponse>> getLinkedAccounts() {
        Long userId = UserPrincipal.getCurrentUserId();
        List<LinkedBankAccountResponse> accounts = bankService.getLinkedAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @DeleteMapping("/unlink/{bankAccountId}")
    @Operation(summary = "Délier un compte", description = "Supprimer la liaison avec un compte bancaire")
    public ResponseEntity<Void> unlinkAccount(@PathVariable Long bankAccountId) {
        Long userId = UserPrincipal.getCurrentUserId();
        bankService.unlinkBankAccount(userId, bankAccountId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer/to-bank")
    @Operation(summary = "Transférer vers banque", description = "Transférer de l'argent vers votre compte bancaire")
    public ResponseEntity<BankTransferResponse> transferToBank(@Valid @RequestBody BankTransferRequest request) {
        Long userId = UserPrincipal.getCurrentUserId();
        BankTransferResponse response = bankService.transferToBank(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/transfer/from-bank")
    @Operation(summary = "Transférer depuis banque", description = "Recharger votre compte depuis votre banque")
    public ResponseEntity<BankTransferResponse> transferFromBank(@Valid @RequestBody BankTransferRequest request) {
        Long userId = UserPrincipal.getCurrentUserId();
        BankTransferResponse response = bankService.transferFromBank(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/transfers/history")
    @Operation(summary = "Historique transferts bancaires", description = "Consulter l'historique des transferts bancaires")
    public ResponseEntity<List<BankTransferResponse>> getHistory() {
        Long userId = UserPrincipal.getCurrentUserId();
        List<BankTransferResponse> history = bankService.getBankTransferHistory(userId);
        return ResponseEntity.ok(history);
    }
}