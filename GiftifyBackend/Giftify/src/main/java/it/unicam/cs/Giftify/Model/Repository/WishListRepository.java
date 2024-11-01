package it.unicam.cs.Giftify.Model.Repository;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    WishList findByUser(Account user);
}
