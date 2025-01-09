package it.unicam.cs.Giftify.Model.Auth;


import it.unicam.cs.Giftify.Model.Entity.RevokedToken;
import it.unicam.cs.Giftify.Model.Entity.Token;
import it.unicam.cs.Giftify.Model.Repository.RevokedTokenRepository;
import it.unicam.cs.Giftify.Model.Repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import it.unicam.cs.Giftify.Model.Entity.Token;

import java.util.Date;

/**
 * Gestore di logout personalizzato per Spring Security.
 * Questo handler revoca i token JWT durante il processo di logout e aggiorna
 * lo stato nel database per evitare l'uso futuro del token revocato.
 */
@Configuration
public class LogoutHandlerImpl implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    /**
     * Esegue il processo di logout.
     * Revoca il token presente nell'intestazione della richiesta e aggiorna il database.
     *
     * @param request        la richiesta HTTP contenente l'intestazione Authorization.
     * @param response       la risposta HTTP.
     * @param authentication i dettagli di autenticazione dell'utente (può essere null in alcuni casi).
     */
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        // Estrae l'intestazione "Authorization" dalla richiesta.
        String authHeader = request.getHeader("Authorization");

        // Se l'intestazione è assente o non contiene un token valido, interrompe l'elaborazione.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        // Estrae il token rimuovendo il prefisso "Bearer ".
        String token = authHeader.substring(7);

        // Crea un oggetto RevokedToken per rappresentare il token revocato.
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(token);
        revokedToken.setTokenType(RevokedToken.TokenType.ACCESS); // Tipo di token (ACCESS).
        revokedToken.setRevokedAt(new Date()); // Imposta la data di revoca al momento attuale.

        // Salva il token revocato nel repository.
        revokedTokenRepository.save(revokedToken);

        // Recupera il token originale dal repository dei token.
        Token storedToken = tokenRepository.findByAccessToken(token).orElse(null);

        // Se il token esiste nel repository, aggiorna il suo stato a "loggedOut".
        if (storedToken != null) {
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }
    }
}

