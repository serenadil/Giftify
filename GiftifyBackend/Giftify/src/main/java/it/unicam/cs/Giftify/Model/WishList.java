package it.unicam.cs.Giftify.Model;

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
    private User user;

    public WishList(User user) {
        wishes= new ArrayList<>();
        this.user = user;
    }

    public WishList() {
    }

}
