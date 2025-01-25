package it.unicam.cs.Giftify.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class AccountCommunityName {
    /**
     * Identificatore univoco dell'assegnazione.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;


    private UUID community;

    private String accountEmail;

    private String userCommunityName;

    public AccountCommunityName(UUID community, String userCommunityName, String accountEmail) {
        this.community = community;
        this.accountEmail = accountEmail;
        this.userCommunityName = userCommunityName;
    }
}
