package com.jeisoneccel.my_finances.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.jeisoneccel.my_finances.config.SecurityConfig.PUBLIC_PATHS;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (filterIsRequired(request)) {
            log.info("Authentication Required");
            authenticationService.authenticate(request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean filterIsRequired(HttpServletRequest request) {
        String path = request.getServletPath();
        return Arrays.stream(PUBLIC_PATHS).noneMatch(p -> {
            if (p.contains("/**")) {
                return path.startsWith(p.replace("/**", ""));
            }
            return path.equals(p);
        });
    }

}
