package com.example.authenticator.controller;

import com.example.authenticator.service.AuthenticatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticator")
public class AuthenticatorController {

    private final AuthenticatorService authenticatorService;

    public AuthenticatorController(AuthenticatorService authenticatorService) {
        this.authenticatorService = authenticatorService;
    }

    @GetMapping("/code")
    public String getAuthenticatorCode(Long epoch) {
        String userId = "1";
        return authenticatorService.createCode(epoch, userId);
    }
}
