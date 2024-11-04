package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    @Getter
    @Setter
    private List<Wish> wishes;
    @ManyToOne
    @Getter
    private Account user;

    public WishList(@NonNull Account user) {
        wishes= new ArrayList<>();
        this.user = user;
    }

    public void addWish(@NonNull Wish wish) {
        if (!wishes.contains(wish)) {
            wishes.add(wish);
        }
    }

    public boolean removeWish(@NonNull Wish wish) {
        return wishes.remove(wish);
    }

}
