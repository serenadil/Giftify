package it.unicam.cs.Giftify.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToMany
    private List<User> userList;

    @Setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String accessCode;
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


    public Group(String groupName, String groupDescription, String groupNote, double budget, LocalDate deadline) {
        userList = new ArrayList<>();
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupNote = groupNote;
        this.budget = budget;
        this.deadline = deadline;
    }


    public Group() {

    }


}
