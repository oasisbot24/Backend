package oasisbot24.oasisapi.repository;

import oasisbot24.oasisapi.domain.EmailVerification;

import java.util.Optional;

public interface EmailVerificationRepository {
    EmailVerification save(EmailVerification emailVerification);

    void updateEmailVerificationIsVerifiedByEmailVerification(String email);

    void updateEmailVerificationToken(String email, String auth);

    Optional<EmailVerification> findById(Long id);

    Optional<EmailVerification> findByEmail(String email);
}
