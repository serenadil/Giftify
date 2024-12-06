package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String token;
    @Setter
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    @Setter
    private Date revokedAt;

    public enum TokenType {
        ACCESS,
        REFRESH
    }

}
