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

import java.util.HashMap;
import java.util.Map;


@RestController
public class AutenticationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private LogoutHandlerImpl logoutHandler;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Registra un nuovo utente con i dati ricevuti nella richiesta.
     *
     * @param request Oggetto contenente i dati di registrazione dell'utente.
     * @return Una risposta contenente il risultato della registrazione.
     */
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            System.out.println("Dati di registrazione ricevuti: " + request);
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (ExistingUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore durante la registrazione" );
        }
    }

    /**
     * Esegue il login dell'utente con le credenziali fornite.
     *
     * @param request Oggetto contenente le credenziali di login dell'utente.
     * @return Una risposta contenente il risultato del login.
     */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("Dati di registrazione ricevuti: " + request);
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops! Sembra che le tue credenziali non siano corrette!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'autenticazione: " );
        }
    }

    /**
     * Esegue il refresh del token di accesso dell'utente.
     *
     * @param request La richiesta HTTP contenente il refresh token.
     * @param response La risposta HTTP in cui restituire il nuovo token.
     * @return Una risposta contenente il nuovo token di accesso.
     */
    @PostMapping("/auth/refresh_token")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return authService.refreshToken(request, response);
    }

    /**
     * Esegue il logout dell'utente autenticato.
     *
     * @param request La richiesta HTTP.
     * @param response La risposta HTTP.
     * @return Una risposta che conferma il logout dell'utente.
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Logout eseguito con successo.");
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Errore durante il logout.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
