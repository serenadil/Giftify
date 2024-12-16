package it.unicam.cs.Giftify.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JsonManagedReference
    private Set<Account> userList;

    private String accessCode;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonBackReference
    private Account admin;

    @Setter
    private String communityName;

    @Setter
    private String communityNote;

    @Setter
    private double budget;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @Setter
    private boolean active;

    @Setter
    private boolean close;

    @OneToMany
    @Setter
    @JsonIgnore
    private Set<GiftAssignment> giftAssignments;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Set<WishList> wishlists;

    public Community(@NonNull AccessCodeGeneretor codeGeneretor, @NonNull Account admin,
                     @NonNull String communityName, String communityNote,
                     double budget, @NonNull LocalDate deadline) {
        userList = new HashSet<>();
        this.accessCode = codeGeneretor.generateCode();
        this.admin = admin;
        this.communityName = communityName;
        this.communityNote = communityNote;
        this.budget = budget;
        this.giftAssignments = new HashSet<>();
        this.wishlists = new HashSet<>();
        if (!this.verifyDeadline(deadline)) {
            this.deadline = deadline;
        } else {
            throw new IllegalArgumentException("deadline non valida");
        }
        this.active = true;
        this.close = false;
    }


    public void addUser(@NonNull Account user, WishList wishList) {
        userList.add(user);
        wishlists.add(wishList);

    }


    public void removeUser(@NonNull Account user) {
        userList.remove(user);
        wishlists.remove(user);
    }


    public boolean verifyDeadline(@NonNull LocalDate deadline) {
        return deadline.isBefore(LocalDate.now());
    }


    public String getGiftReceiver(@NonNull Account giver) {
        String receiver = null;
        for (GiftAssignment giftAssignment : giftAssignments) {
            if (giftAssignment.getGiverEmail().equals(giver.getEmail())) {
                receiver = giftAssignment.getReceiverEmail();
            }
        }
        return receiver;
    }

    public WishList getuserWishList(@NonNull Account user) {
        Set<WishList> wishLists = wishlists;
        WishList wishListUser = null;
        for (WishList wishList : wishLists) {
            if (wishList.getUser().equals(user)) {
                wishListUser = wishList;
            }
        }
        return wishListUser;
    }

}
