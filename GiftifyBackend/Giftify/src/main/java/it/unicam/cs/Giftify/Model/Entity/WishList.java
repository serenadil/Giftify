package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Setter
    private Set<Wish> wishes;

    @ManyToOne
    private Account user;

    public WishList(@NonNull Account user) {
        wishes = new HashSet<>();
        this.user = user;
    }

    public void addWish(@NonNull Wish wish) {
        wishes.add(wish);
    }

    public void removeWish(@NonNull Wish wish) {
        wishes.remove(wish);
    }

}
