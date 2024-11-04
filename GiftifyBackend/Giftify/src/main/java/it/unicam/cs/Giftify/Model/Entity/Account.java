package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @OneToMany
    private List<Community> userCommunities;

    public Account(@NonNull String email, @NonNull String password) {
        setEmail(email);
        setPassword(password);
        userCommunities = new ArrayList<>();
    }

    public void setEmail(@NonNull String email) {
        if (validaEmail(email)) {
            throw new IllegalArgumentException("Email non valida.");
        }
        this.email = email;
    }


    public void setPassword(@NonNull String password) {
        if (validaPassword(password)) {
            throw new IllegalArgumentException("Password non valida.");
        }
        this.password = password;
    }

    private static boolean validaEmail(@NonNull String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return !pattern.matcher(email).matches();
    }

    private static boolean validaPassword(@NonNull String password) {
        return password.length() < 8 || !password.matches(".*[0-9].*");
    }

    public void addCommunity(@NonNull Community community) {
        userCommunities.add(community);
    }

    public void removeCommunity(@NonNull Community community) {
        userCommunities.remove(community);
    }


}
