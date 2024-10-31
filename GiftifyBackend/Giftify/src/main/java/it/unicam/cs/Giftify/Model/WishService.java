package it.unicam.cs.Giftify.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    @Autowired
    private WishRepository wishRepository;
    @Autowired
    private WishListRepository wishListRepository;

    public Wish createWish(String name, String imagePath) {
        return new Wish(name, imagePath);
    }

    public Wish getWish(String name) {
        return wishRepository.findByName(name);
    }

    public void deleteWish(Wish wish, WishList wishList) {
        wishRepository.delete(wish);
        wishList.removeWish(wish);
        wishListRepository.save(wishList);
    }
}
