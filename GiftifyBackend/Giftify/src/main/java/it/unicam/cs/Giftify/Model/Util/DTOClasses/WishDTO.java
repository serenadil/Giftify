package it.unicam.cs.Giftify.Model.Util.DTOClasses;

import it.unicam.cs.Giftify.Model.Entity.WishList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishDTO {
    private String name;
    private String imagePath;
}
