package it.unicam.cs.Giftify.Model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
@Entity
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String email;


    private String password;


    public void setEmail(String email) {
        if (validaEmail(email)) {
            throw new IllegalArgumentException("Email non valida.");
        }
        this.email = email;
    }


    public void setPassword(String password) {
        if (validaPassword(password)) {
            throw new IllegalArgumentException("Password non valida.");
        }
        this.password = password;
    }

    public static boolean validaEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email == null || !pattern.matcher(email).matches();
    }

    public static boolean validaPassword(String password) {
        return password == null || password.length() < 8 || !password.matches(".*[0-9].*");
    }


}
