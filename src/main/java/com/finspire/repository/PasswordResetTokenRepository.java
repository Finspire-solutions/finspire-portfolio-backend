package com.finspire.repository;

import com.finspire.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    Optional<PasswordResetToken> findByResetToken(String token);
    void deleteByResetToken(String token);

    Optional<PasswordResetToken> findByUserEmail(String email);
}
