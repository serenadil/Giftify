package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Repository.WishRepository;
import it.unicam.cs.Giftify.Model.Entity.Wish;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    @Autowired
    private WishRepository wishRepository;
    @Autowired
    private WishListService wishListService;

    public Wish createWish(String name, String imagePath, WishList wishList) {
        return new Wish(name, imagePath, wishList);
    }

    public Wish getWish(String name) {
        return wishRepository.findByName(name);
    }

    public void deleteWish(Wish wish, WishList wishList) {
        wishList.removeWish(wish);
        wishListService.updateWishList(wishList);
    }
}
