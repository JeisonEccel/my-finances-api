package com.jeisoneccel.my_finances.auth;

import com.jeisoneccel.my_finances.auth.models.LoginModel;
import com.jeisoneccel.my_finances.auth.models.RefreshModel;
import com.jeisoneccel.my_finances.auth.models.RegistrationModel;
import com.jeisoneccel.my_finances.auth.models.ValidateModel;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationModel model, HttpServletRequest request) {
        return new ResponseEntity<>(service.register(model, request), HttpStatus.OK);
    }

    @PostMapping({"/login", "/login/"})
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginModel model, HttpServletRequest request) {
        return new ResponseEntity<>(service.login(model, request), HttpStatus.OK);
    }

    @PostMapping({"/refresh", "/refresh/"})
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RefreshModel model, HttpServletRequest request) {
        return new ResponseEntity<>(service.refresh(model, request), HttpStatus.OK);
    }

    @PostMapping({"/validate", "/validate"})
    public ResponseEntity<AuthenticationResponse> validate(@RequestBody ValidateModel model, HttpServletRequest request) {
        return new ResponseEntity<>(service.validate(model, request), HttpStatus.OK);
    }


}
