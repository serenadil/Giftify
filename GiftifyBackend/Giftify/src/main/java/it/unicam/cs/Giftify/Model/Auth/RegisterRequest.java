package it.unicam.cs.Giftify.Model.Auth;

/**
 * Record per rappresentare una richiesta di registrazione.
 *
 * @param password Password dell'utente.
 * @param email    Email dell'utente.
 * @param username Username dell'utente.
 */
public record RegisterRequest(String password, String email, String username) {
}

