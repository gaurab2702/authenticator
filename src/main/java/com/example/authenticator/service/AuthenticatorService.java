package com.example.authenticator.service;

import org.springframework.stereotype.Service;

@Service
public class AuthenticatorService {

    public String createCode(Long epoch, String userId) {
        return "123456";
    }
}
