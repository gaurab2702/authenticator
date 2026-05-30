package com.example.authenticator.controller;

import com.example.authenticator.service.AuthenticatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authenticator")
public class AuthenticatorController {

    private final AuthenticatorService authenticatorService;

    public AuthenticatorController(AuthenticatorService authenticatorService) {
        this.authenticatorService = authenticatorService;
    }

    @PostMapping("/secret_key")
    public ResponseEntity<String> saveSecretKey(@RequestParam String siteName,
                                                @RequestParam String secretKey) {
        authenticatorService.saveSecretKey(siteName, secretKey);
        return ResponseEntity.ok("Secret key saved for site: " + siteName);
    }

    @GetMapping("/codes")
    public Map<String, String> getAllCodes() {
        return authenticatorService.getAllCodes();
    }

    @GetMapping("/code")
    public String getAuthenticatorCode(@RequestParam String siteName) {
        return authenticatorService.createCode(System.currentTimeMillis() / 1000, siteName);
    }
}
