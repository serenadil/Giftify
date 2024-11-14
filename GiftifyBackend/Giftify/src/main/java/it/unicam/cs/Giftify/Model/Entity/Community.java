package it.unicam.cs.Giftify.Model.Entity;

import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Account> userList;

    private String accessCode;

    @OneToOne
    private Account admin;

    @Setter
    private String communityName;

    @Setter
    private String communityNote;

    @Setter
    private double budget;

    @Setter
    private LocalDate deadline;

    @Setter
    private boolean active;

    @Setter
    private boolean close;

    @OneToMany
    @Setter
    private Map<Account, Account> giftAssignments;
    @OneToMany
    private Map<Account, WishList> wishlists;

    public Community(@NonNull AccessCodeGeneretor codeGeneretor, @NonNull Account admin,
                     @NonNull String communityName, @NonNull String communityDescription, String communityNote,
                     double budget, @NonNull LocalDate deadline) {
        userList = new ArrayList<>();
        this.accessCode = codeGeneretor.generateCode();
        this.admin = admin;
        this.communityName = communityName;
        this.communityNote = communityNote;
        this.budget = budget;
        this.wishlists = new HashMap<>();
        if (this.verifyDeadline(deadline)) {
            this.deadline = deadline;
        } else {
            throw new IllegalArgumentException("");
        }
        this.active = true;
        this.close = false;
    }


    public void addUser(@NonNull Account user, WishList wishList) {
        userList.add(user);
        wishlists.put(user,wishList );

    }


    public void removeUser(@NonNull Account user) {
        userList.remove(user);
        wishlists.remove(user);
    }


    public boolean verifyDeadline(@NonNull LocalDate deadline) {
        return this.deadline.isAfter(LocalDate.now());
    }


    public Account getGiftReceiver(@NonNull Account giver) {
        return giftAssignments.get(giver);
    }

    public WishList getuserWishList(@NonNull Account user) {
        return wishlists.get(user);
    }


}
