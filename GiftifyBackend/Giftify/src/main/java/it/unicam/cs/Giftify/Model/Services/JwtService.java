package it.unicam.cs.Giftify.Model.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.RevokedToken;
import it.unicam.cs.Giftify.Model.Repository.RevokedTokenRepository;
import it.unicam.cs.Giftify.Model.Repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;


/**
 * Servizio per gestire la generazione, la validazione e la gestione dei token JWT.
 */
 @Service
public class JwtService {

    @Value("${app.jwt.key}")
    private String key;

    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpire;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    /**
     * Genera un token di accesso per un determinato utente.
     *
     * @param user l'utente per cui generare il token
     * @return il token di accesso generato
     */
    public String generateAccessToken(Account user) {
        return generateToken(user, accessTokenExpire);
    }

    /**
     * Genera un token di refresh per un determinato utente.
     *
     * @param user l'utente per cui generare il token di refresh
     * @return il token di refresh generato
     */
    public String generateRefreshToken(Account user) {
        return generateToken(user, refreshTokenExpire);
    }

    /**
     * Metodo privato per generare un token JWT con i parametri specificati.
     *
     * @param user       l'utente per cui generare il token
     * @param expireTime il tempo di scadenza del token in millisecondi
     * @return il token JWT generato
     */
    private String generateToken(Account user, long expireTime) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getAuthorities().iterator().next().getAuthority());
        return generateToken(extraClaims, user, expireTime);
    }

    /**
     * Genera un token JWT con una serie di claims aggiuntive.
     *
     * @param extraClaims claims aggiuntivi da includere nel token
     * @param userDetails i dettagli dell'utente
     * @param expiration  il tempo di scadenza del token in millisecondi
     * @return il token JWT generato
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Verifica se un token di accesso è valido per un dato utente.
     *
     * @param token il token di accesso da verificare
     * @param user  l'utente con cui confrontare il token
     * @return true se il token è valido, altrimenti false
     */
    public boolean isTokenValid(String token, UserDetails user) {
        String username = extractUsername(token);
        if (isTokenRevoked(token)) {
            return false;
        }
        boolean validToken = tokenRepository
                .findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);
        return username.equals(user.getUsername()) && !isTokenExpired(token) && validToken;
    }

    /**
     * Verifica se un token è stato revocato.
     *
     * @param token il token da verificare
     * @return true se il token è revocato, altrimenti false
     */
    boolean isTokenRevoked(String token) {
        Optional<RevokedToken> revokedToken = revokedTokenRepository.findByToken(token);
        return revokedToken.isPresent();
    }

    /**
     * Verifica se un token di refresh è valido per un dato utente.
     *
     * @param token il token di refresh da verificare
     * @param user  l'utente con cui confrontare il token
     * @return true se il token di refresh è valido, altrimenti false
     */
    public boolean isValidRefreshToken(String token, Account user) {
        String username = extractUsername(token);

        boolean validRefreshToken = tokenRepository
                .findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return username.equals(user.getName()) && !isTokenExpired(token) && validRefreshToken;
    }

    /**
     * Verifica se un token è scaduto.
     *
     * @param token il token da verificare
     * @return true se il token è scaduto, altrimenti false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Estrae la data di scadenza di un token.
     *
     * @param token il token da cui estrarre la scadenza
     * @return la data di scadenza del token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Estrae un claim da un token.
     *
     * @param token    il token da cui estrarre il claim
     * @param resolver la funzione per risolvere il claim
     * @param <T>      il tipo del claim
     * @return il claim estratto dal token
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Estrae tutti i claims da un token.
     *
     * @param token il token da cui estrarre i claims
     * @return i claims estratti dal token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Estrae il nome utente (subject) da un token.
     *
     * @param token il token da cui estrarre il nome utente
     * @return il nome utente estratto dal token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Ottiene la chiave di firma utilizzata per firmare i token.
     *
     * @return la chiave di firma
     */
    public Key getSignInKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }
}

