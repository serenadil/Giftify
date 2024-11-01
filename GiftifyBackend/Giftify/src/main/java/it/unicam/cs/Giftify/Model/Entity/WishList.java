package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
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

    public WishList(Account user) {
        wishes= new ArrayList<>();
        this.user = user;
    }

    public WishList() {
    }

    public void addWish(Wish wish) {
        if (!wishes.contains(wish)) {
            wishes.add(wish);
        }
    }

    public boolean removeWish(Wish wish) {
        return wishes.remove(wish);
    }

}
