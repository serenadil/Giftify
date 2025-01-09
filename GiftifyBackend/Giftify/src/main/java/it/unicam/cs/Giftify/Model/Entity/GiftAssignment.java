package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Classe che rappresenta l'assegnazione di un regalo in una comunità.
 * Contiene informazioni sul donatore e sul destinatario.
 */
@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class GiftAssignment {

    /**
     * Identificatore univoco dell'assegnazione.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email dell'utente che effettua il regalo.
     */
    private String giverEmail;

    /**
     * Email dell'utente che riceve il regalo.
     */
    private String receiverEmail;

    /**
     * Comunità a cui appartiene l'assegnazione.
     */
    @ManyToOne
    private Community community;

    /**
     * Costruttore per creare una nuova assegnazione di regalo.
     *
     * @param giver     Email del donatore.
     * @param receiver  Email del destinatario.
     * @param community Comunità di appartenenza.
     */
    public GiftAssignment(String giver, String receiver, Community community) {
        this.giverEmail = giver;
        this.receiverEmail = receiver;
        this.community = community;
    }
}
