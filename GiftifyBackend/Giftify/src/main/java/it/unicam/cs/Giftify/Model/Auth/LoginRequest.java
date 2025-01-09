package it.unicam.cs.Giftify.Model.Auth;

/**
 * Record per rappresentare una richiesta di login.
 *
 * @param email    Email dell'utente.
 * @param password Password dell'utente.
 */
public record LoginRequest(String email, String password) {
}