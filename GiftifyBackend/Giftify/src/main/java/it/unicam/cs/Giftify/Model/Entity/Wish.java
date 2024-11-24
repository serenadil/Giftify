package it.unicam.cs.Giftify.Model.Entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Setter
    private String imagePath;

    @Setter
    @ManyToOne
    private WishList wishList;

    public Wish(@NonNull String name, @NonNull String imagePath, @NonNull WishList wishList) {
        this.name = name;
        this.imagePath = imagePath;
        this.wishList = wishList;
    }
}
