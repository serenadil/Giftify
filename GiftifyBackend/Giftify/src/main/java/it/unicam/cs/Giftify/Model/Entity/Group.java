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
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<User> userList;

    private String accessCode;

    @OneToOne
    private User adminGroup;

    @Setter
    private String groupName;

    @Setter
    private String groupDescription;

    @Setter
    private String groupNote;

    @Setter
    private double budget;

    @Setter
    private LocalDate deadline;
    @Setter
    private boolean active;


    public Group(AccessCodeGeneretor codeGeneretor, User adminGroup, String groupName, String groupDescription, String groupNote, double budget, LocalDate deadline) {
        userList = new ArrayList<>();
        this.accessCode = codeGeneretor.generateCode();
        this.adminGroup = adminGroup;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupNote = groupNote;
        this.budget = budget;
        if (this.verifyDeadline(deadline)) {
            this.deadline = deadline;
        } else {
            throw new IllegalArgumentException("");
        }
        this.active = true;
    }


    public Group() {

    }

    public void addUser(User user) {
        userList.add(user);
    }


    public void removeUser(User user) {
        userList.remove(user);
    }


    public boolean verifyDeadline(LocalDate deadline) {
        return this.deadline.isAfter(LocalDate.now());
    }


}
