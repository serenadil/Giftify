package it.unicam.cs.Giftify.Model.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Risposta di autenticazione.
 * Contiene i token di accesso, il token di aggiornamento e un messaggio opzionale.
 */
public class AuthResponse {
    @Getter
    @JsonProperty("access_token")
    private String accessToken;
    @Getter
    @JsonProperty("refresh_token")
    private String refreshToken;
    @Getter
    @JsonProperty("message")
    private String message;

    /**
     * Costruttore per creare una risposta di autenticazione.
     *
     * @param accessToken  Token di accesso.
     * @param refreshToken Token di aggiornamento.
     * @param message      Messaggio opzionale.
     */
    public AuthResponse(String accessToken, String refreshToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
        this.refreshToken = refreshToken;
    }


}
