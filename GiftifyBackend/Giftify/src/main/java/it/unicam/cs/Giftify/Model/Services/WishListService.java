package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Repository.WishListRepository;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private WishService wishService;


    public WishList getWishList(Account user) {
        return wishListRepository.findByUser(user);
    }

    public void updateWishList(WishList wishList) {
        this.wishListRepository.save(wishList);
    }

}
