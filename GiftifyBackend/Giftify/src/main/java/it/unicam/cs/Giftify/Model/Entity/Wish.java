package it.unicam.cs.Giftify.Model.Entity;


import jakarta.persistence.*;
import lombok.*;

/**
 * Classe che rappresenta un desiderio nella wishlist di un utente.
 * Ogni desiderio è associato a un nome, un percorso immagine e a una wishlist.
 */
@Entity
@NoArgsConstructor
@Getter
public class Wish {

    /**
     * Identificatore univoco del desiderio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome del desiderio.
     */
    @Setter
    private String name;

    /**
     * Percorso dell'immagine associata al desiderio.
     */
    @Setter
    private String imagePath;

    /**
     * Wishlist a cui appartiene il desiderio.
     */
    @Setter
    @ManyToOne
    private WishList wishList;

    /**
     * Costruttore per creare un nuovo desiderio.
     *
     * @param name      Nome del desiderio.
     * @param imagePath Percorso immagine associato al desiderio.
     * @param wishList  Wishlist di appartenenza.
     */
    public Wish(@NonNull String name, @NonNull String imagePath, @NonNull WishList wishList) {
        this.name = name;
        this.imagePath = imagePath;
        this.wishList = wishList;
    }
}

