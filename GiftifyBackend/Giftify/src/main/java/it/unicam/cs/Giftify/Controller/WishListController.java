package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/wishlist/create")
    public ResponseEntity<String> createWishList() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account user = (Account) authentication.getPrincipal();
            WishList wishList = wishListService.createWishList(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Wishlist creata con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la creazione della wishlist: " + e.getMessage());
        }
    }

    @PostMapping("/wishlist/addWish/{wishId}")
    public ResponseEntity<String> addWishToWishList(@PathVariable Long wishId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account user = (Account) authentication.getPrincipal();
            WishList wishList = wishListService.getWishList(user);
            if (wishList != null) {
                return ResponseEntity.ok("Desiderio aggiunto con successo alla tua wishlist.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La wishlist non è stata trovata.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'aggiunta del desiderio: " + e.getMessage());
        }
    }
}
