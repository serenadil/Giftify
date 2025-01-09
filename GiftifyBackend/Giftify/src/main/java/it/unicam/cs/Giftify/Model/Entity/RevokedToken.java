package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Classe che rappresenta un token revocato.
 */
@Entity
@Getter
public class RevokedToken {

    /**
     * Identificatore univoco del token revocato.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Valore del token revocato.
     */
    @Setter
    private String token;

    /**
     * Tipo del token revocato (ACCESS o REFRESH).
     */
    @Setter
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    /**
     * Data in cui il token è stato revocato.
     */
    @Setter
    private Date revokedAt;

    /**
     * Tipi di token (ACCESS e REFRESH).
     */
    public enum TokenType {
        ACCESS,
        REFRESH
    }
}
