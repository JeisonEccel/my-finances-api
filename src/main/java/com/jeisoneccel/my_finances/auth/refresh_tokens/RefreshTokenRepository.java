package com.jeisoneccel.my_finances.auth.refresh_tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByTokenAndExpiresAtAfter(@NonNull String token, ZonedDateTime expiresAfter);

}
