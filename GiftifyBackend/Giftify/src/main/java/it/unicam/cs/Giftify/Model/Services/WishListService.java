package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
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

    public void addWish(Wish wish) {
        wish.getWishList().addWish(wish);
        wishListRepository.save(wish.getWishList());
    }

    public WishList getWishList(Account user) {
        return wishListRepository.findByUser(user);
    }

    public void removeWishFromWishList(WishList wishList, Wish wish) {
        wishList.removeWish(wish);
        wishListRepository.save(wishList);
    }

    public void updateWishList(WishList wishList) {
        wishListRepository.save(wishList);
    }

    public void deleteWishList(WishList wishList) {
        wishListRepository.delete(wishList);
    }
}
