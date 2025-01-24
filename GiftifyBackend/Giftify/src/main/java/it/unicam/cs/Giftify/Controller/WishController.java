package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Entity.Wish;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
import it.unicam.cs.Giftify.Model.Services.WishListService;
import it.unicam.cs.Giftify.Model.Services.WishService;
import it.unicam.cs.Giftify.Model.Util.DTOClasses.WishDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;
/**
 * Classe Controller per gestire le operazioni relative ai desideri.
 */
@RestController
public class WishController {

    @Autowired
    private WishService wishService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CommunityService communityService;
    @Autowired
    private WishListService wishListService;

    /**
     * Aggiunge un desiderio alla wishlist di un utente in una community specifica.
     *
     * @param wishDTO i dettagli del desiderio (nome e categoria)
     * @param id      l'UUID della community
     * @return un messaggio di successo o errore
     */
    @PostMapping("/wish/addWish/{id}")
    public ResponseEntity<String> addWish(@RequestBody WishDTO wishDTO, @PathVariable UUID id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account user = (Account) authentication.getPrincipal();
            user = accountService.getAccountById(user.getId());
            Community community = communityService.getCommunityById(id);
            if (user.getRoleForCommunity(community) == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            wishService.createWish(wishDTO.getName(), wishDTO.getCategory(), community.getuserWishList(community.getCommunityNameByAccount(user)));
            System.out.println(community.getuserWishList(community.getCommunityNameByAccount(user)).getWishes().size());
            return ResponseEntity.status(HttpStatus.CREATED).body("Desiderio aggiunto con successo alla tua lista");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }

    /**
     * Elimina un desiderio dalla wishlist di un utente.
     *
     * @param id l'ID del desiderio da eliminare
     * @return un messaggio di successo o errore
     */
    @DeleteMapping("/wish/deleteWish/{communityId}/{id}")
    public ResponseEntity<String> deleteWish(@PathVariable Long id, @PathVariable UUID communityId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account user = (Account) authentication.getPrincipal();
            user = accountService.getAccountById(user.getId());
            Optional<Wish> wish = wishService.getWish(id);
            Community community =communityService.getCommunityById(communityId);
            if (community.getuserWishList(community.getCommunityNameByAccount(user)) == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            if (wish.isPresent()) {
                wishService.deleteWish(wish.get());
                return ResponseEntity.ok("Wish deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Desiderio non trovato");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }

    /**
     * Modifica i dettagli di un desiderio esistente.
     *
     * @param wishDTO i nuovi dettagli del desiderio (nome e categoria)
     * @param id      l'ID del desiderio da modificare
     * @return un messaggio di successo o errore
     */
    @PutMapping("/wish/editWish/{communityId}/{id}")
    public ResponseEntity<String> editWish(@RequestBody WishDTO wishDTO, @PathVariable Long id, @PathVariable UUID communityId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account user = (Account) authentication.getPrincipal();
            user = accountService.getAccountById(user.getId());
            Optional<Wish> wishOptional = wishService.getWish(id);

            if (wishOptional.isPresent()) {
                Wish wish = wishOptional.get();
                Community community =communityService.getCommunityById(communityId);
                if (community.getuserWishList(community.getCommunityNameByAccount(user)) == null) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
                if (wishDTO.getName() != null) {
                    wish.setName(wishDTO.getName());
                }
                if (wishDTO.getCategory() != null) {
                    wish.setCategory(wishDTO.getCategory());
                }
                wishService.updateWish(wish);
                return ResponseEntity.ok("Desiderio aggiornato con successo");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Desiderio non trovato");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }

}
