package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe che rappresenta un token di autenticazione e il suo stato.
 */
@Entity
public class Token {

    /**
     * Identificatore univoco del token.
     */
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Token di accesso associato.
     */
    @Setter
    @Getter
    private String accessToken;

    /**
     * Token di aggiornamento associato.
     */
    @Setter
    @Getter
    private String refreshToken;

    /**
     * Stato del token, indica se l'utente si è disconnesso.
     */
    @Setter
    @Getter
    private boolean loggedOut;

    /**
     * Account associato a questo token.
     */
    @Setter
    @Getter
    @ManyToOne
    private Account account;
}
