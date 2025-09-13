package com.jeisoneccel.my_finances.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeisoneccel.my_finances.auth.models.LoginModel;
import com.jeisoneccel.my_finances.auth.models.RegistrationModel;
import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.classes.users.UserModel;
import com.jeisoneccel.my_finances.classes.users.UserService;
import com.jeisoneccel.my_finances.exceptions.RequestError;
import com.jeisoneccel.my_finances.utils.JwtUtils;
import com.jeisoneccel.my_finances.utils.ServiceUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static com.jeisoneccel.my_finances.exceptions.ErrorCode.*;
import static com.jeisoneccel.my_finances.exceptions.ExceptionType.BAD_CREDENTIALS;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String AUTH_HEADER = "Authorization";

    private final ServiceUtils serviceUtils;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public AuthenticationResponse register(RegistrationModel model) {
        log.info("Registering a new user with email: {}", model.email());
        String refreshToken = UUID.randomUUID().toString();

        UserModel userModel = serviceUtils.mapModelToEntity(model, new UserModel());
        userModel.setRefreshToken(refreshToken);
        User user = userService.create(userModel);

        String accessToken = jwtUtils.generateToken(user.getEmail());
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse login(LoginModel model) {
        User user = userService.loadUserByUsername(model.username());

        if (!passwordEncoder.matches(model.password(), user.getPassword())) {
            throw new BadCredentialsException(ERR0A00004.name());
        }

        String refreshToken = UUID.randomUUID().toString();
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("refreshToken", refreshToken);
        userService.update(user.getId(), updates);

        String accessToken = jwtUtils.generateToken(user.getEmail());
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public void authenticate(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("Authenticating request: {}", request.getServletPath());
        String bearerToken = request.getHeader(AUTH_HEADER);

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            RequestError requestError = new RequestError(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS, ERR0A00005.name());
            sendErrorResponse(response, requestError);
            return;
        }

        String token = bearerToken.substring(7);

        try {
            String username = jwtUtils.extractUsername(token);
            User user = userService.loadUserByUsername(username);

            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            RequestError requestError = new RequestError(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS, ERR0A00006.name());
            sendErrorResponse(response, requestError);
        } catch (JwtException e) {
            RequestError requestError = new RequestError(HttpStatus.UNAUTHORIZED, BAD_CREDENTIALS, ERR0A00007.name());
            sendErrorResponse(response, requestError);
        } catch (Exception e) {
            e.printStackTrace();
            filterChain.doFilter(request, response);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, RequestError requestError) {
        try {
            response.setStatus(requestError.getHttpStatus().value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String responseBody = objectMapper.writeValueAsString(requestError);
            response.getWriter().write(responseBody);
            response.getWriter().flush();
        } catch (IOException e) {
            System.out.println("Could not write response error");
        }
    }

}
