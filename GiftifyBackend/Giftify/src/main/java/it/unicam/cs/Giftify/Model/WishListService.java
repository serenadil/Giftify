package it.unicam.cs.Giftify.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private WishService wishService;

    public WishList getWishList(User user) {
        return wishListRepository.findByUser(user);
    }

    public void updateWishList(WishList wishList) {
        this.wishListRepository.save(wishList);
    }

}
