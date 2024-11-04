package it.unicam.cs.Giftify.Model.Entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String imagePath;
    @Getter
    @Setter
    @ManyToOne
    private WishList wishList;

    public Wish(@NonNull String name, @NonNull String imagePath, @NonNull WishList wishList) {
        this.name = name;
        this.imagePath = imagePath;
        this.wishList = wishList;
    }
}
