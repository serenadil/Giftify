package it.unicam.cs.Giftify.Model.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
     * Categoria di immagini associata al desiderio.
     */
    @Setter
    private WishCategory category;
    /**
     * Wishlist a cui appartiene il desiderio.
     */
    @Setter
    private long wishList;

    /**
     * Costruttore per creare un nuovo desiderio.
     *
     * @param name      Nome del desiderio.
     * @param category Categoria di immagini associata al desiderio.
     * @param wishList  Wishlist di appartenenza.
     */
    public Wish(@NonNull String name, @NonNull WishCategory category, long wishList) {
        this.name = name;
        this.category = category;
        this.wishList = wishList;
    }
}

