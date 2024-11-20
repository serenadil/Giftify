package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class AccountCommunityRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @ManyToOne
    private Account account;
    @Setter
    @ManyToOne
    private Community community;
    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    public AccountCommunityRole(Account account, Community community, Role role) {
        this.account = account;
        this.community = community;
        this.role = role;
    }


}
