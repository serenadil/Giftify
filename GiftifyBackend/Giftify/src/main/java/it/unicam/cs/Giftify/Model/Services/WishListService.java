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

    /**
     * Crea una nuova lista dei desideri per un utente.
     *
     * @param user L'utente per il quale creare la lista dei desideri.
     * @return La lista dei desideri appena creata.
     */
    public WishList createWishList(Account user) {
        WishList wishList = new WishList(user);
        wishListRepository.save(wishList);
        return wishList;
    }

    /**
     * Aggiunge un desiderio alla lista dei desideri.
     *
     * @param wish Il desiderio da aggiungere.
     */
    public void addWish(Wish wish) {
        wish.getWishList().addWish(wish);
        wishListRepository.save(wish.getWishList());
    }

    /**
     * Ottiene la lista dei desideri associata ad un utente.
     *
     * @param user L'utente di cui ottenere la lista dei desideri.
     * @return La lista dei desideri dell'utente.
     */
    public WishList getWishList(Account user) {
        return wishListRepository.findByUser(user);
    }


    public void removeWishFromWishList(Wish wish) {
        wish.getWishList().removeWish(wish);
        wishListRepository.save(wish.getWishList());}

    /**
     * Rimuove un desiderio dalla lista dei desideri.
     *
     * @param wishList La lista dei desideri da cui rimuovere il desiderio.
     * @param wish Il desiderio da rimuovere.
     */
    public void removeWishFromWishList(WishList wishList, Wish wish) {
        wishList.removeWish(wish);
        wishListRepository.save(wishList);
    }

    /**
     * Aggiorna la lista dei desideri nel database.
     *
     * @param wishList La lista dei desideri da aggiornare.
     */
    public void updateWishList(WishList wishList) {
        wishListRepository.save(wishList);
    }

    /**
     * Elimina una lista dei desideri dal database.
     *
     * @param wishList La lista dei desideri da eliminare.
     */
    public void deleteWishList(WishList wishList) {
        wishListRepository.delete(wishList);
    }
}
