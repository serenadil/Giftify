package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Wish;
import it.unicam.cs.Giftify.Model.Repository.WishListRepository;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    public WishList createWishList(Account user) {
        WishList wishList = new WishList(user);
        wishListRepository.save(wishList);
        return wishList;
    }

    public void addWish (Wish wish) {
        if (wish.getWishList().getWishes().contains(wish)) {
            wish.getWishList().addWish(wish);
            wishListRepository.save(wish.getWishList());
        } else throw new IllegalArgumentException("Questo desiderio è già presente nella lista!");
    }

    public WishList getWishList(Account user) {
        return wishListRepository.findByUser(user);
    }

    public void updateWishList(WishList wishList) {
        this.wishListRepository.save(wishList);
    }

    public void deleteWishList(WishList wishList) {
        this.wishListRepository.delete(wishList);
    }

}
