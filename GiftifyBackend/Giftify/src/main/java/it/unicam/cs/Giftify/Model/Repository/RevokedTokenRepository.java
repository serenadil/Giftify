package it.unicam.cs.Giftify.Model.Repository;

import it.unicam.cs.Giftify.Model.Entity.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {
    Optional<RevokedToken> findByToken(String token);
}
