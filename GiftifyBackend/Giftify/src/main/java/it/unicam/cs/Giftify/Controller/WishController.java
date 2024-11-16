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
@RequestMapping("/wish")
public class WishController {

    @Autowired
    private WishService wishService;

    @PostMapping("/create")
    public ResponseEntity<String> createWish(@RequestParam WishDTO wishDTO) {
        try {
            wishService.createWish(wishDTO.getName(), wishDTO.getImagePath(), wishDTO.getWishList());
            return ResponseEntity.status(HttpStatus.CREATED).body("Wish created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Wish");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteWish(@RequestParam Long id) {
        try {
            Optional<Wish> wish = wishService.getWish(id);
            if (wish.isPresent()) {
                wishService.deleteWish(wish.get(), wish.get().getWishList());
                return ResponseEntity.ok("Wish deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wish or WishList not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Wish");
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editWish(@RequestParam WishDTO wishDTO) {
        try {
            wishService.editWish(wishDTO.getId(), wishDTO.getName(), wishDTO.getImagePath());
            return ResponseEntity.ok("Wish edited successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error editing Wish");
        }
    }
}
