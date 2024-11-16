package it.unicam.cs.Giftify.Model.Util.DTOClasses;

import it.unicam.cs.Giftify.Model.Entity.WishList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishDTO {
    private String name;
    private String imagePath;
    private WishList wishList;
    private Long id;
}
