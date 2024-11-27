package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class GiftAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String giverEmail;


    private String receiverEmail;

    @ManyToOne
    private Community community;

    public GiftAssignment(String giver, String receiver, Community community) {
        this.giverEmail = giver;
        this.receiverEmail = receiver;
        this.community = community;
    }
}