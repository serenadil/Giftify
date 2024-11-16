package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Auth.AuthResponse;
import it.unicam.cs.Giftify.Model.Auth.ExistingUserException;
import it.unicam.cs.Giftify.Model.Auth.LoginRequest;
import it.unicam.cs.Giftify.Model.Auth.RegisterRequest;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutenticationController {

    @Autowired
    private AccountService accountServices;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws ExistingUserException {
        return ResponseEntity.ok(accountServices.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(accountServices.login(request));
    }
}


