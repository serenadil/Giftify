package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Auth.AuthResponse;
import it.unicam.cs.Giftify.Model.Auth.ExistingUserException;
import it.unicam.cs.Giftify.Model.Auth.LoginRequest;
import it.unicam.cs.Giftify.Model.Auth.RegisterRequest;
import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Token;
import it.unicam.cs.Giftify.Model.Repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servizio per gestire l'autenticazione degli utenti, la registrazione, e la gestione dei token JWT.
 */
@Service
public class AuthService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Registra un nuovo utente e restituisce i token di accesso e di aggiornamento.
     *
     * @param request la richiesta di registrazione contenente email, password e nome utente
     * @return un oggetto AuthResponse contenente i token generati
     * @throws ExistingUserException se un account con la stessa email esiste già
     */
    public AuthResponse register(RegisterRequest request) throws ExistingUserException {
        Account account = accountService.createAccount(request.email(), passwordEncoder.encode(request.password()), request.username());
        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);
        saveUserToken(accessToken, refreshToken, account);
        return new AuthResponse(accessToken, refreshToken, "Utente registrato con successo");
    }

    /**
     * Autentica un utente con email e password, e restituisce i token di accesso e di aggiornamento.
     *
     * @param request la richiesta di login contenente email e password
     * @return un oggetto AuthResponse contenente i token generati
     */
    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        Account account = accountService.getAccount(request.email())
                .orElseThrow(() -> new BadCredentialsException(request.email()));
        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        revokeAllTokenByUser(account);
        saveUserToken(accessToken, refreshToken, account);
        return new AuthResponse(accessToken, refreshToken, "Bentornato!");
    }

    /**
     * Revoca tutti i token di accesso associati a un determinato utente.
     *
     * @param user l'utente i cui token devono essere revocati
     */
    private void revokeAllTokenByUser(Account user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByAccount(user);
        if (validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(t -> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }

    /**
     * Salva i token generati per un determinato utente nel database.
     *
     * @param accessToken  il token di accesso
     * @param refreshToken il token di aggiornamento
     * @param account      l'account dell'utente per cui vengono generati i token
     */
    private void saveUserToken(String accessToken, String refreshToken, Account account) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setAccount(account);
        tokenRepository.save(token);
    }

    /**
     * Rinnova i token di accesso e aggiornamento, restituendo un nuovo set di token.
     *
     * @param request  la richiesta contenente l'header Authorization con il token di refresh
     * @param response la risposta HTTP
     * @return una risposta HTTP con i nuovi token generati, o uno stato UNAUTHORIZED se il token non è valido
     */
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        Account account = accountService.getAccount(username)
                .orElseThrow(() -> new RuntimeException("No user found"));
        if (jwtService.isTokenRevoked(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if (jwtService.isValidRefreshToken(token, account)) {
            String accessToken = jwtService.generateAccessToken(account);
            String refreshToken = jwtService.generateRefreshToken(account);

            revokeAllTokenByUser(account);
            saveUserToken(accessToken, refreshToken, account);

            return new ResponseEntity(new AuthResponse(accessToken, refreshToken, "New token generated"), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}