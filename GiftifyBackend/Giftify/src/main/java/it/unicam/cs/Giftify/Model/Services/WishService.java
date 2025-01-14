package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Entity.*;
import it.unicam.cs.Giftify.Model.Repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import it.unicam.cs.Giftify.Model.Entity.Wish;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import it.unicam.cs.Giftify.Model.Repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WishService {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private WishListService wishListService;

    /**
     * Crea un nuovo desiderio e lo aggiunge alla lista dei desideri associata.
     *
     * @param name Il nome del desiderio.
     * @param category Categoria di immagini associata al desiderio.
     * @param wishList La lista dei desideri alla quale aggiungere il desiderio.
     */
    public void createWish(String name, WishCategory category, WishList wishList) {
        Wish wish = new Wish(name, category, wishList);
        wishRepository.save(wish);
        wishListService.addWish(wish);
    }

    /**
     * Ottiene un desiderio tramite il suo ID.
     *
     * @param id L'ID del desiderio.
     * @return Un oggetto Optional che contiene il desiderio, se trovato.
     */
    public Optional<Wish> getWish(Long id) {
        return wishRepository.findById(id);
    }

    /**
     * Elimina un desiderio dalla lista dei desideri.
     *
     * @param wish Il desiderio da eliminare.
     */
    public void deleteWish(Wish wish) {

        wishListService.removeWishFromWishList(wish);

        wish.getWishList().removeWish(wish);

    }

    /**
     * Aggiorna le informazioni di un desiderio.
     *
     * @param wish Il desiderio da aggiornare.
     */
    public void updateWish(Wish wish) {
        wishRepository.save(wish);
        wishListService.updateWishList(wish.getWishList());
    }
}

