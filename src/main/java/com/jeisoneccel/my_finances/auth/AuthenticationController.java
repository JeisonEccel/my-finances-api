package com.jeisoneccel.my_finances.auth;

import com.jeisoneccel.my_finances.auth.models.LoginModel;
import com.jeisoneccel.my_finances.auth.models.RegistrationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping({"/register", "/register/"})
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationModel model) {
        return new ResponseEntity<>(service.register(model), HttpStatus.OK);
    }

    @PostMapping({"/login", "/login/"})
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginModel model) {
        return new ResponseEntity<>(service.login(model), HttpStatus.OK);
    }

}
