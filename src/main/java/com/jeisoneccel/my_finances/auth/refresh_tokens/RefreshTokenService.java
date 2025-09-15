package com.jeisoneccel.my_finances.auth.refresh_tokens;

import com.jeisoneccel.my_finances.classes.users.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String TYPE = RefreshToken.class.getSimpleName();

    private final RefreshTokenRepository repository;

    public RefreshToken create(HttpServletRequest request, User user) {
        log.info(TYPE + ": Creating new with user ({})", user.getEmail());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(ZonedDateTime.now().plusMonths(1));
        refreshToken.setDevice(request.getHeader("User-Agent"));
        refreshToken.setIpAddress(request.getRemoteAddr());

        return repository.save(refreshToken);
    }

}
