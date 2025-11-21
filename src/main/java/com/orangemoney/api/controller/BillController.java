package com.orangemoney.api.controller;

import com.orangemoney.api.dto.request.BillPaymentRequest;
import com.orangemoney.api.dto.response.BillResponse;
import com.orangemoney.api.security.UserPrincipal;
import com.orangemoney.api.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
@Tag(name = "Bills", description = "Paiement de factures")
@SecurityRequirement(name = "bearerAuth")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/pay")
    @Operation(summary = "Payer une facture", description = "Payer SENELEC, SEN'EAU, Canal+, etc.")
    public ResponseEntity<BillResponse> payBill(@Valid @RequestBody BillPaymentRequest request) {
        Long userId = UserPrincipal.getCurrentUserId();
        BillResponse response = billService.payBill(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    @Operation(summary = "Historique factures", description = "Consulter l'historique des paiements de factures")
    public ResponseEntity<List<BillResponse>> getHistory() {
        Long userId = UserPrincipal.getCurrentUserId();
        List<BillResponse> history = billService.getBillHistory(userId);
        return ResponseEntity.ok(history);
    }
}