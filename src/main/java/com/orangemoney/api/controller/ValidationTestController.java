package com.orangemoney.api.controller;

import com.orangemoney.api.validation.annotation.ValidPhoneNumber;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class ValidationTestController {

    @PostMapping("/validate-phone")
    public ResponseEntity<String> validatePhone(@Valid @RequestBody PhoneTestRequest request) {
        return ResponseEntity.ok("Numéro valide : " + request.getPhoneNumber());
    }

    public static class PhoneTestRequest {
        @ValidPhoneNumber
        private String phoneNumber;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}