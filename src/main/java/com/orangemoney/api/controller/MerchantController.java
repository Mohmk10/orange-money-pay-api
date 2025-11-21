package com.orangemoney.api.controller;

import com.orangemoney.api.dto.request.MerchantPaymentRequest;
import com.orangemoney.api.dto.response.MerchantPaymentResponse;
import com.orangemoney.api.security.UserPrincipal;
import com.orangemoney.api.service.MerchantPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchant")
@Tag(name = "Merchant", description = "Paiements marchands")
@SecurityRequirement(name = "bearerAuth")
public class MerchantController {

    private final MerchantPaymentService merchantPaymentService;

    public MerchantController(MerchantPaymentService merchantPaymentService) {
        this.merchantPaymentService = merchantPaymentService;
    }

    @PostMapping("/pay")
    @Operation(summary = "Payer un marchand", description = "Effectuer un paiement chez un marchand")
    public ResponseEntity<MerchantPaymentResponse> payMerchant(@Valid @RequestBody MerchantPaymentRequest request) {
        Long userId = UserPrincipal.getCurrentUserId();
        MerchantPaymentResponse response = merchantPaymentService.payMerchant(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    @Operation(summary = "Historique paiements marchands", description = "Consulter l'historique des paiements marchands")
    public ResponseEntity<List<MerchantPaymentResponse>> getHistory() {
        Long userId = UserPrincipal.getCurrentUserId();
        List<MerchantPaymentResponse> history = merchantPaymentService.getMerchantPaymentHistory(userId);
        return ResponseEntity.ok(history);
    }
}