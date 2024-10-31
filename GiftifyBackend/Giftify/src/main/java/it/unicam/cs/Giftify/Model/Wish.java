package it.unicam.cs.Giftify.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String imagePath;

    public Wish(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }
    public Wish() {
    }
}
