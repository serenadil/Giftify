package it.unicam.cs.Giftify.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Classe che rappresenta un account utente nel sistema.
 * Implementa l'interfaccia UserDetails per l'integrazione con Spring Security.
 */
@Getter
@Entity
@NoArgsConstructor
public class Account implements UserDetails {

    /**
     * Identificatore univoco dell'account.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome dell'utente associato all'account.
     */
    @Setter
    private String name;

    /**
     * Email dell'utente.
     */
    private String email;

    /**
     * Password dell'account.
     */
    private String password;

    /**
     * Insieme delle comunità a cui l'utente appartiene.
     */
    @ManyToMany
    @JsonIgnore
    @Getter
    private Set<Community> userCommunities;

    /**
     * Insieme dei ruoli associati all'utente nelle varie comunità.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<AccountCommunityRole> communityRoles;

    /**
     * Costruttore per creare un nuovo account.
     *
     * @param email    Email dell'utente.
     * @param password Password dell'utente.
     * @param username Nome utente.
     * @throws IllegalArgumentException se l'email o la password non sono valide.
     */
    public Account(@NonNull String email, @NonNull String password, @NonNull String username) {
        setEmail(email);
        setPassword(password);
        this.name = username;
        userCommunities = new HashSet<>();
        communityRoles = new HashSet<>();
    }

    /**
     * Imposta l'email dell'account dopo averne verificato la validità.
     *
     * @param email Email da impostare.
     * @throws IllegalArgumentException se l'email non è valida.
     */
    public void setEmail(@NonNull String email) {
        if (!checkEmail(email)) {
            throw new IllegalArgumentException("Email non valida.");
        }
        this.email = email;
    }

    /**
     * Imposta la password dell'account dopo averne verificato la validità.
     *
     * @param password Password da impostare.
     * @throws IllegalArgumentException se la password non è valida.
     */
    public void setPassword(@NonNull String password) {
        if (checkPassword(password)) {
            throw new IllegalArgumentException("Password non valida.");
        }
        this.password = password;
    }

    /**
     * Verifica la validità di un'email.
     *
     * @param email Email da verificare.
     * @return true se l'email è valida, false altrimenti.
     */
    private static boolean checkEmail(@NonNull String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * Verifica la validità di una password.
     *
     * @param password Password da verificare.
     * @return true se la password è valida, false altrimenti.
     */
    private static boolean checkPassword(@NonNull String password) {
        return password.length() < 8 || !password.matches(".*[0-9].*");
    }

    /**
     * Aggiunge una comunità all'elenco delle comunità dell'utente.
     *
     * @param community Comunità da aggiungere.
     */
    public void addCommunity(@NonNull Community community) {
        userCommunities.add(community);
    }

    /**
     * Rimuove una comunità dall'elenco delle comunità dell'utente.
     *
     * @param community Comunità da rimuovere.
     */
    public void removeCommunity(@NonNull Community community) {
        userCommunities.remove(community);
    }

    /**
     * Ottiene il ruolo dell'utente per una comunità specifica.
     *
     * @param community Comunità di interesse.
     * @return Il ruolo dell'utente nella comunità o {@link Role#STANDARD} se non specificato.
     */
    public Role getRoleForCommunity(Community community) {
        return communityRoles.stream()
                .filter(role -> role.getCommunity().equals(community))
                .map(AccountCommunityRole::getRole)
                .findFirst()
                .orElse(Role.STANDARD);
    }

    /**
     * Aggiunge o aggiorna il ruolo dell'utente in una comunità.
     *
     * @param accountCommunityRole Ruolo da aggiungere o aggiornare.
     */
    public void addOrUpdateRoleForCommunity(AccountCommunityRole accountCommunityRole) {
        communityRoles.add(accountCommunityRole);
    }

    /**
     * Rimuove il ruolo dell'utente per una comunità specifica.
     *
     * @param community Comunità di interesse.
     */
    public void removeRoleForCommunity(@NonNull Community community) {
        Optional<AccountCommunityRole> roleToRemove = communityRoles.stream()
                .filter(role -> role.getCommunity().equals(community))
                .findFirst();
        roleToRemove.ifPresent(communityRoles::remove);
    }

    /**
     * Ottiene le autorità assegnate all'utente per le comunità a cui appartiene.
     *
     * @return Una collezione di {@link GrantedAuthority}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_STANDARD"));
        if (communityRoles != null) {
            for (AccountCommunityRole role : communityRoles) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole().name() + "_" + role.getCommunity().getId()));
            }
        }
        return authorities;
    }

    /**
     * Restituisce l'email come nome utente.
     *
     * @return L'email dell'utente.
     */
    @Override
    public String getUsername() {
        return this.getEmail();
    }

    /**
     * Indica se l'account è scaduto.
     *
     * @return true se l'account non è scaduto.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se l'account è bloccato.
     *
     * @return true se l'account non è bloccato.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se le credenziali dell'account sono scadute.
     *
     * @return true se le credenziali non sono scadute.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se l'account è abilitato.
     *
     * @return true se l'account è abilitato.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
