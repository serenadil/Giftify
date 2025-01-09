package it.unicam.cs.Giftify.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JsonBackReference
    private Community community;

    @ManyToOne
    private Account account;

    private String userCommunityName;

    public AccountCommunityName(Community community, String userCommunityName, Account account) {
        this.community = community;
        this.account = account;
        this.userCommunityName = userCommunityName;
    }
}
