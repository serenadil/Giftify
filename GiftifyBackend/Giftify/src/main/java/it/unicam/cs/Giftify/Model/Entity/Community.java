package it.unicam.cs.Giftify.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Classe che rappresenta una comunità virtuale.
 * Ogni comunità ha un amministratore, un elenco di utenti, un budget,
 * una data di scadenza per lo scambio dei regali e altre informazioni di configurazione.
 */
@Getter
@NoArgsConstructor
@Entity
public class Community {

    /**
     * Identificatore univoco della comunità.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Elenco degli utenti che fanno parte della comunità.
     */
    @ManyToMany
    @JsonManagedReference
    private Set<Account> userList;

    /**
     * Codice di accesso univoco per la comunità.
     */
    private String accessCode;

    /**
     * Amministratore della comunità.
     */
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonBackReference
    private Account admin;

    /**
     * Nome della comunità.
     */
    @Getter
    @Setter
    private String communityName;

    /**
     * Nota descrittiva della comunità.
     */
    @Setter
    private String communityNote;

    /**
     * Budget stabilito per i regali della comunità.
     */
    @Setter
    private double budget;

    /**
     * Data di scadenza per lo scambio dei regali.
     */
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    /**
     * Stato della comunità: attiva o inattiva.
     */
    @Setter
    private boolean active;

    /**
     * Indica se la comunità è chiusa.
     */
    @Setter
    private boolean close;

    /**
     * Assegnazioni dei regali per la comunità.
     */
    @OneToMany
    @Setter
    @JsonIgnore
    private Set<GiftAssignment> giftAssignments;

    /**
     * Elenco delle wishlist degli utenti nella comunità.
     */
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Set<WishList> wishlists;

    /**
     * Costruttore per creare una nuova comunità.
     *
     * @param codeGeneretor Generatore per il codice di accesso.
     * @param admin         Amministratore della comunità.
     * @param communityName Nome della comunità.
     * @param communityNote Nota descrittiva della comunità.
     * @param budget        Budget stabilito per i regali.
     * @param deadline      Data di scadenza per lo scambio dei regali.
     * @throws IllegalArgumentException se la data di scadenza è nel passato.
     */
    public Community(@NonNull AccessCodeGeneretor codeGeneretor, @NonNull Account admin,
                     @NonNull String communityName, String communityNote,
                     double budget, @NonNull LocalDate deadline) {
        userList = new HashSet<>();
        this.accessCode = codeGeneretor.generateCode();
        this.admin = admin;
        this.communityName = communityName;
        this.communityNote = communityNote;
        this.budget = budget;
        this.giftAssignments = new HashSet<>();
        this.wishlists = new HashSet<>();
        if (!this.verifyDeadline(deadline)) {
            this.deadline = deadline;
        } else {
            throw new IllegalArgumentException("deadline non valida");
        }
        this.active = true;
        this.close = false;
    }

    /**
     * Aggiunge un utente e la sua wishlist alla comunità.
     *
     * @param user     Utente da aggiungere.
     * @param wishList Wishlist dell'utente.
     */
    public void addUser(@NonNull Account user, WishList wishList) {
        userList.add(user);
        wishlists.add(wishList);
    }

    /**
     * Rimuove un utente e la sua wishlist dalla comunità.
     *
     * @param user Utente da rimuovere.
     */
    public void removeUser(@NonNull Account user) {
        userList.remove(user);
        wishlists.remove(user);
    }

    /**
     * Verifica se la data di scadenza è valida (non nel passato).
     *
     * @param deadline Data di scadenza da verificare.
     * @return true se la data è nel passato, false altrimenti.
     */
    public boolean verifyDeadline(@NonNull LocalDate deadline) {
        return deadline.isBefore(LocalDate.now());
    }

    /**
     * Ottiene l'email del destinatario del regalo assegnato a un utente.
     *
     * @param giver Utente che fa il regalo.
     * @return Email del destinatario del regalo o null se non trovato.
     */
    public String getGiftReceiver(@NonNull Account giver) {
        String receiver = null;
        for (GiftAssignment giftAssignment : giftAssignments) {
            if (giftAssignment.getGiverEmail().equals(giver.getEmail())) {
                receiver = giftAssignment.getReceiverEmail();
            }
        }
        return receiver;
    }

    /**
     * Ottiene la wishlist di un utente specifico.
     *
     * @param user Utente di cui recuperare la wishlist.
     * @return La wishlist dell'utente o null se non trovata.
     */
    public WishList getuserWishList(@NonNull Account user) {
        Set<WishList> wishLists = wishlists;
        WishList wishListUser = null;
        for (WishList wishList : wishLists) {
            if (wishList.getUser().equals(user)) {
                wishListUser = wishList;
            }
        }
        return wishListUser;
    }
}
