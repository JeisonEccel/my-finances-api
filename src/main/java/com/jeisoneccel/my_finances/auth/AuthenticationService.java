package com.jeisoneccel.my_finances.auth;

import com.jeisoneccel.my_finances.auth.models.RegistrationModel;
import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.classes.users.UserModel;
import com.jeisoneccel.my_finances.classes.users.UserService;
import com.jeisoneccel.my_finances.utils.JwtUtils;
import com.jeisoneccel.my_finances.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ServiceUtils serviceUtils;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthenticationResponse register(RegistrationModel model) {
        log.info("Registering a new user with email: {}", model.email());
        String refreshToken = UUID.randomUUID().toString();

        UserModel userModel = serviceUtils.mapModelToEntity(model, new UserModel());
        userModel.setRefreshToken(refreshToken);
        User user = userService.create(userModel);

        String accessToken = jwtUtils.generateToken(user.getEmail());
        return new AuthenticationResponse(accessToken, refreshToken);
    }
}
