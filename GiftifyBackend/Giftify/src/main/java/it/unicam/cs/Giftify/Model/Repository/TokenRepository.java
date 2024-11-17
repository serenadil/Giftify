package it.unicam.cs.Giftify.Model.Repository;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllAccessTokensByAccount(Account account);

    Optional<Token> findByAccessToken(String token);

    Optional<Token > findByRefreshToken(String token);
}
