package com.orangemoney.api.controller;

import com.orangemoney.api.dto.request.QRPaymentRequest;
import com.orangemoney.api.dto.response.QRCodeResponse;
import com.orangemoney.api.dto.response.TransferResponse;
import com.orangemoney.api.security.UserPrincipal;
import com.orangemoney.api.service.QRCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/qrcode")
@Tag(name = "QR Code", description = "Génération et paiement par QR Code")
@SecurityRequirement(name = "bearerAuth")
public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/generate")
    @Operation(summary = "Générer mon QR Code", description = "Générer un QR Code pour recevoir de l'argent")
    public ResponseEntity<QRCodeResponse> generateMyQRCode() {
        Long userId = UserPrincipal.getCurrentUserId();
        QRCodeResponse response = qrCodeService.generateUserQRCode(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate/merchant")
    @Operation(summary = "Générer QR Code marchand", description = "Générer un QR Code avec montant pour demande de paiement")
    public ResponseEntity<QRCodeResponse> generateMerchantQRCode(
            @RequestParam String amount,
            @RequestParam String reference) {
        Long userId = UserPrincipal.getCurrentUserId();
        QRCodeResponse response = qrCodeService.generateMerchantQRCode(userId, amount, reference);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/scan")
    @Operation(summary = "Scanner et payer", description = "Scanner un QR Code et effectuer le paiement")
    public ResponseEntity<TransferResponse> scanAndPay(@Valid @RequestBody QRPaymentRequest request) {
        Long userId = UserPrincipal.getCurrentUserId();
        TransferResponse response = qrCodeService.scanAndPay(userId, request);
        return ResponseEntity.ok(response);
    }
}