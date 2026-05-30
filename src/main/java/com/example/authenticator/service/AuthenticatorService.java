package com.example.authenticator.service;

import com.example.authenticator.entity.UserSecret;
import com.example.authenticator.repository.UserSecretRepository;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticatorService {

    private static final int TIME_STEP = 60;
    private static final int CODE_DIGITS = 6;

    private final UserSecretRepository userSecretRepository;

    public AuthenticatorService(UserSecretRepository userSecretRepository) {
        this.userSecretRepository = userSecretRepository;
    }

    public void saveSecretKey(String siteName, String secretKey) {
        UserSecret userSecret = new UserSecret();
        userSecret.setSiteName(siteName);
        userSecret.setSecretKey(secretKey);
        userSecretRepository.save(userSecret);
    }

    public Map<String, String> getAllCodes() {
        List<UserSecret> secrets = userSecretRepository.findAll();
        long timeStep = System.currentTimeMillis() / 1000 / TIME_STEP;
        Map<String, String> codes = new LinkedHashMap<>();
        for (UserSecret secret : secrets) {
            codes.put(secret.getSiteName(), generateTotp(secret.getSecretKey(), timeStep));
        }
        return codes;
    }

    public String createCode(Long epoch, String siteName) {
        UserSecret userSecret = userSecretRepository.findById(siteName)
                .orElseThrow(() -> new IllegalArgumentException("No secret found for siteName: " + siteName));

        long timeStep = epoch / TIME_STEP;
        return generateTotp(userSecret.getSecretKey(), timeStep);
    }

    private String generateTotp(String base64SecretKey, long timeStep) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64SecretKey);
            byte[] timeBytes = ByteBuffer.allocate(8).putLong(timeStep).array();

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(keyBytes, "HmacSHA1"));
            byte[] hmac = mac.doFinal(timeBytes);

            int offset = hmac[hmac.length - 1] & 0x0F;
            int code = ((hmac[offset] & 0x7F) << 24)
                    | ((hmac[offset + 1] & 0xFF) << 16)
                    | ((hmac[offset + 2] & 0xFF) << 8)
                    | (hmac[offset + 3] & 0xFF);

            int otp = code % (int) Math.pow(10, CODE_DIGITS);
            return String.format("%0" + CODE_DIGITS + "d", otp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate TOTP", e);
        }
    }
}
