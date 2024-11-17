package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Token {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String accessToken;

    @Setter
    @Getter
    private String refreshToken;

    @Setter
    @Getter
    private boolean loggedOut;

    @Setter
    @Getter
    @ManyToOne
    private Account account;


}
