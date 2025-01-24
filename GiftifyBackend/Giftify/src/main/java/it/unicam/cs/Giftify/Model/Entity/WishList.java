package it.unicam.cs.Giftify.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe che rappresenta una wishlist associata a un utente.
 * Contiene un insieme di desideri e un riferimento all'utente proprietario.
 */
@Entity
@NoArgsConstructor
@Getter
public class WishList {

    /**
     * Identificatore univoco della wishlist.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    /**
     * Insieme dei desideri presenti nella wishlist.
     * I desideri sono rimossi automaticamente se eliminati dalla wishlist.
     */
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "wishList")
    @JsonManagedReference
    @Setter
    private Set<Wish> wishes;

    /**
     * Utente proprietario della wishlist.
     */

    private String userEmail;

    /**
     * Costruttore per creare una nuova wishlist associata a un utente.
     *
     * @param userEmail Utente proprietario della wishlist.
     */
    public WishList(@NonNull String userEmail) {
        wishes = new HashSet<>();
        this.userEmail = userEmail;
    }

    /**
     * Aggiunge un desiderio alla wishlist.
     *
     * @param wish Desiderio da aggiungere.
     */
    public void addWish(@NonNull Wish wish) {
        wishes.add(wish);
    }

    /**
     * Rimuove un desiderio dalla wishlist.
     *
     * @param wish Desiderio da rimuovere.
     */
    public void removeWish(@NonNull Wish wish) {
        wishes.remove(wish);
    }
}
