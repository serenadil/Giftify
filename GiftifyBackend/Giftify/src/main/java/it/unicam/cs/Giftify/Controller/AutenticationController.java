package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Auth.AuthResponse;
import it.unicam.cs.Giftify.Model.Auth.ExistingUserException;
import it.unicam.cs.Giftify.Model.Auth.LoginRequest;
import it.unicam.cs.Giftify.Model.Auth.RegisterRequest;
import it.unicam.cs.Giftify.Model.Services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutenticationController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) throws ExistingUserException {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.refreshToken(request, response);
    }
}


