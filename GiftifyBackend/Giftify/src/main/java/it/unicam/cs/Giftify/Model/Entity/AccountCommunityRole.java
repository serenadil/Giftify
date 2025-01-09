package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long id;

    /**
     * Account a cui è associato il ruolo.
     */
    @Setter
    @ManyToOne
    private Account account;

    /**
     * Comunità a cui è associato il ruolo.
     */
    @Setter
    @ManyToOne
    private Community community;

    /**
     * Ruolo specifico dell'utente nella comunità.
     */
    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Costruttore per creare un'associazione tra account, comunità e ruolo.
     *
     * @param account   Account dell'utente.
     * @param community Comunità di appartenenza.
     * @param role      Ruolo dell'utente nella comunità.
     */
    public AccountCommunityRole(Account account, Community community, Role role) {
        this.account = account;
        this.community = community;
        this.role = role;
    }
}
