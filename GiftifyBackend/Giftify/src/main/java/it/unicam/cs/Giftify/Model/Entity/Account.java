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

@Getter
@Entity
@NoArgsConstructor
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;

    private String email;

    private String password;

    @ManyToMany
    @JsonIgnore
    @Getter
    private Set<Community> userCommunities;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<AccountCommunityRole> communityRoles;

    public Account(@NonNull String email, @NonNull String password, @NonNull String username) {
        setEmail(email);
        setPassword(password);
        this.name = username;
        userCommunities = new HashSet<>();
        communityRoles = new HashSet<>();
    }

    public void setEmail(@NonNull String email) {
        if (!checkEmail(email)) {
            throw new IllegalArgumentException("Email non valida.");
        }
        this.email = email;
    }


    public void setPassword(@NonNull String password) {
        if (checkPassword(password)) {
            throw new IllegalArgumentException("Password non valida.");
        }
        this.password = password;
    }

    private static boolean checkEmail(@NonNull String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private static boolean checkPassword(@NonNull String password) {
        return password.length() < 8 || !password.matches(".*[0-9].*");
    }

    public void addCommunity(@NonNull Community community) {
        userCommunities.add(community);
    }

    public void removeCommunity(@NonNull Community community) {
        userCommunities.remove(community);
    }


    public Role getRoleForCommunity(Community community) {
        return communityRoles.stream()
                .filter(role -> role.getCommunity().equals(community))
                .map(AccountCommunityRole::getRole)
                .findFirst()
                .orElse(Role.STANDARD);
    }


    public void addOrUpdateRoleForCommunity(AccountCommunityRole accountCommunityRole) {
        communityRoles.add(accountCommunityRole);

    }


    public void removeRoleForCommunity(@NonNull Community community) {
        Optional<AccountCommunityRole> roleToRemove = communityRoles.stream()
                .filter(role -> role.getCommunity().equals(community))
                .findFirst();
        roleToRemove.ifPresent(communityRoles::remove);
    }


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

    @Override
    public String getUsername() {
        return this.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
