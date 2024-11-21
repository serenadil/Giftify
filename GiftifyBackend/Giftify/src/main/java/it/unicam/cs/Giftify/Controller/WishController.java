package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Entity.Wish;
import it.unicam.cs.Giftify.Model.Services.WishService;
import it.unicam.cs.Giftify.Model.Util.DTOClasses.WishDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
public class WishController {

    @Autowired
    private WishService wishService;

    @PostMapping("/addWish")
    public ResponseEntity<String> addWish(@RequestParam WishDTO wishDTO) {
        wishService.createWish(wishDTO.getName(), wishDTO.getImagePath(), wishDTO.getWishList());
        return ResponseEntity.status(HttpStatus.CREATED).body("Wish created successfully");

    }

    @DeleteMapping("/deleteWish/{id}")
    public ResponseEntity<String> deleteWish(@PathVariable Long id) {
        Optional<Wish> wish = wishService.getWish(id);
        if (wish.isPresent()) {
            wishService.deleteWish(wish.get());
            return ResponseEntity.ok("Wish deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wish or WishList not found");
        }

    }

    @PutMapping("/editWish/{id}")
    public ResponseEntity<String> editWish(@RequestParam WishDTO wishDTO, @PathVariable Long id) {
        Optional<Wish> wishOptional = wishService.getWish(id);
        if (wishOptional.isPresent()) {
            Wish wish = wishOptional.get();
            if (wishDTO.getName() != null) {
                wish.setName(wishDTO.getName());
            }
            if (wishDTO.getImagePath() != null) {
                wish.setImagePath(wishDTO.getImagePath());
            }
            if (wishDTO.getWishList() != null) {
                wish.setWishList(wishDTO.getWishList());
            }
            wishService.updateWish(wish);
            return ResponseEntity.ok("Wish updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wish not found");
        }
    }


}
