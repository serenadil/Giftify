package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Auth.*;
import it.unicam.cs.Giftify.Model.Repository.AccountRepository;
import it.unicam.cs.Giftify.Model.Repository.RevokedTokenRepository;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
public class AutenticationController {
    @Autowired
    private AuthService authService;


    @Autowired
    private LogoutHandlerImpl logoutHandler;

    @Autowired
    private RevokedTokenRepository tokenRepository;


    @Autowired
    private AccountRepository accountRepository;


    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            System.out.println("Dati di registrazione ricevuti: " + request);
            AuthResponse response = authService.register(request);

            System.out.println(accountRepository.findAll().get(0).getEmail());
            return ResponseEntity.ok(response);
        } catch (ExistingUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore durante la registrazione" + e.getMessage());
        }
    }


    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("Dati di registrazione ricevuti: " + request);
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops! Sembra che le tue credenziali non siano corrette!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'autenticazione: " + e.getMessage());
        }
    }


    @PostMapping("/auth/refresh_token")
    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.refreshToken(request, response);
    }


    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            return ResponseEntity.ok("Logout eseguito con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il logout: " + e.getMessage());
        }
    }

}


