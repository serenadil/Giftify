package it.unicam.cs.Giftify.Model.Entity;

import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
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
    private String communityDescription;

    @Setter
    private String communityNote;

    @Setter
    private double budget;

    @Setter
    private LocalDate deadline;

    @Setter
    private boolean active;


    public Community(AccessCodeGeneretor codeGeneretor, Account admin, String communityName, String communityDescription, String communityNote, double budget, LocalDate deadline) {
        userList = new ArrayList<>();
        this.accessCode = codeGeneretor.generateCode();
        this.admin = admin;
        this.communityName = communityName;
        this.communityDescription = communityDescription;
        this.communityNote = communityNote;
        this.budget = budget;
        if (this.verifyDeadline(deadline)) {
            this.deadline = deadline;
        } else {
            throw new IllegalArgumentException("");
        }
        this.active = true;
    }


    public Community() {

    }

    public void addUser(Account user) {
        userList.add(user);
    }


    public void removeUser(Account user) {
        userList.remove(user);
    }


    public boolean verifyDeadline(LocalDate deadline) {
        return this.deadline.isAfter(LocalDate.now());
    }


}
