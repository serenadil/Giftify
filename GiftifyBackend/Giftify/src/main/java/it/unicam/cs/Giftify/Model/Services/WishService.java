package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Repository.WishRepository;
import it.unicam.cs.Giftify.Model.Entity.Wish;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WishService {

    @Autowired
    private WishRepository wishRepository;
    @Autowired
    private WishListService wishListService;

    public void createWish(String name, String imagePath, WishList wishList) {
        Wish wish = new Wish(name, imagePath, wishList);
        wishRepository.save(wish);
        wishListService.addWish(wish);
    }

    public Optional<Wish> getWish(Long id) {
        return wishRepository.findById(id);
    }


    public void deleteWish(Wish wish, WishList wishList) {
        wishList.removeWish(wish);
        wishListService.updateWishList(wishList);
    }

    public void editWish(Long id, String name, String imagePath) {
        Wish wish = wishRepository.findById(id).orElse(null);
        assert wish != null;
        if (wish.getWishList().getWishes().contains(wish)) {
            wish.setName(name);
            wish.setImagePath(imagePath);
            wishRepository.save(wish);
            wishListService.updateWishList(wish.getWishList());
        }
    }
}
