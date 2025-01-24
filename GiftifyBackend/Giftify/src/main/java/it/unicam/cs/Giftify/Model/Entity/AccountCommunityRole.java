package it.unicam.cs.Giftify.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Classe che rappresenta il ruolo di un utente all'interno di una comunità.
 */
@Entity
@Getter
@NoArgsConstructor
public class AccountCommunityRole {
    /**
     * Identificatore univoco del ruolo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    /**
     * Account a cui è associato il ruolo.
     */
    @Setter
    private String accountEmail;

    /**
     * Comunità a cui è associato il ruolo.
     */
    @Setter
    private UUID community;

    /**
     * Ruolo specifico dell'utente nella comunità.
     */
    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Costruttore per creare un'associazione tra account, comunità e ruolo.
     *
     * @param accountEmail Account dell'utente.
     * @param community    Comunità di appartenenza.
     * @param role         Ruolo dell'utente nella comunità.
     */
    public AccountCommunityRole(String accountEmail, UUID community, Role role) {
        this.accountEmail = accountEmail;
        this.community = community;
        this.role = role;
    }
}
