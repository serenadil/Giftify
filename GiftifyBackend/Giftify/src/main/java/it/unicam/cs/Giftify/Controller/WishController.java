package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Entity.Wish;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
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

@RestController
@RequestMapping
public class WishController {

    @Autowired
    private WishService wishService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CommunityService communityService;

    @PostMapping("/addWish/{id}")
    public ResponseEntity<String> addWish(@RequestBody WishDTO wishDTO, @PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account user = (Account) authentication.getPrincipal();
        user = accountService.getAccountById(user.getId());
        Community community = communityService.getCommunityById(id);
        if (user.getRoleForCommunity(community) == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        wishService.createWish(wishDTO.getName(), wishDTO.getImagePath(), community.getuserWishList(user));
        return ResponseEntity.status(HttpStatus.CREATED).body("Wish created successfully");

    }

    @DeleteMapping("/deleteWish/{id}")
    public ResponseEntity<String> deleteWish(@PathVariable Long id) {
        Optional<Wish> wish = wishService.getWish(id);
        if (wish.isPresent()) {
            wishService.deleteWish(wish.get());
            return ResponseEntity.ok("Wish deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wish not found");
        }

    }

    @PutMapping("/editWish/{id}")
    public ResponseEntity<String> editWish(@RequestBody WishDTO wishDTO, @PathVariable Long id) {
        Optional<Wish> wishOptional = wishService.getWish(id);
        if (wishOptional.isPresent()) {
            Wish wish = wishOptional.get();
            if (wishDTO.getName() != null) {
                wish.setName(wishDTO.getName());
            }
            if (wishDTO.getImagePath() != null) {
                wish.setImagePath(wishDTO.getImagePath());
            }
            wishService.updateWish(wish);
            return ResponseEntity.ok("Wish updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wish not found");
        }
    }


}
