package com.orangemoney.api.controller;

import com.orangemoney.api.dto.request.RechargeRequest;
import com.orangemoney.api.dto.response.RechargeResponse;
import com.orangemoney.api.security.UserPrincipal;
import com.orangemoney.api.service.RechargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recharge")
@Tag(name = "Recharge", description = "Recharge de crédit, internet et forfaits")
@SecurityRequirement(name = "bearerAuth")
public class RechargeController {

    private final RechargeService rechargeService;

    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @PostMapping
    @Operation(summary = "Effectuer une recharge", description = "Recharger crédit, internet ou Illimix")
    public ResponseEntity<RechargeResponse> recharge(@Valid @RequestBody RechargeRequest request) {
        Long userId = UserPrincipal.getCurrentUserId();
        RechargeResponse response = rechargeService.performRecharge(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    @Operation(summary = "Historique recharges", description = "Consulter l'historique des recharges")
    public ResponseEntity<List<RechargeResponse>> getHistory() {
        Long userId = UserPrincipal.getCurrentUserId();
        List<RechargeResponse> history = rechargeService.getRechargeHistory(userId);
        return ResponseEntity.ok(history);
    }
}