package com.example.authenticator.repository;

import com.example.authenticator.entity.UserSecret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSecretRepository extends JpaRepository<UserSecret, String> {
}
