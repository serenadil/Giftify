package it.unicam.cs.Giftify.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Group {

    @OneToMany
    private List<User> userList;

    private String accessCode;

    private String groupName;

    private String groupDescription;

    private String groupNote;

    private double budget;

    private LocalDate deadline;


    public Group(String groupName, String groupDescription, String groupNote, double budget, LocalDate deadline) {
        userList = new ArrayList<>();
        AccessCodeGeneretor accessCodeGeneretor = new AccessCodeGeneretor();
        accessCode = accessCodeGeneretor.generateCode();
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupNote = groupNote;
        this.budget = budget;
        this.deadline = deadline;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Group() {

    }


    public Long getId() {
        return id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;

    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupNote(String groupNote) {
        this.groupNote = groupNote;
    }

    public String getGroupNote() {
        return groupNote;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getBudget() {
        return budget;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;

    }

    public LocalDate getDeadline() {
        return deadline;

    }
}
