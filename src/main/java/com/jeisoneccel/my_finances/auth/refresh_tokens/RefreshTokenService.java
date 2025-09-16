package com.jeisoneccel.my_finances.auth.refresh_tokens;

import com.jeisoneccel.my_finances.classes.users.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.jeisoneccel.my_finances.exceptions.ErrorCode.ERR0A00008;

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

    public RefreshToken getByToken(String token) {
        log.info(TYPE + ": Fetching by token ({})", token);
        return repository.findByTokenAndExpiresAtAfter(token, ZonedDateTime.now()).orElseThrow(
                () -> new BadCredentialsException(ERR0A00008.name())
        );
    }

    public RefreshToken revokeToken(String token) {
        log.info(TYPE + ": Revoking token ({})", token);
        RefreshToken refreshToken = getByToken(token);
        refreshToken.setExpiresAt(ZonedDateTime.now());
        return repository.save(refreshToken);
    }

}
